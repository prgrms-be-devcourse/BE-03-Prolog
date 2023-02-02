package com.prgrms.prolog.domain.post.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.prolog.domain.post.dto.PostDto.CreatePostRequest;
import com.prgrms.prolog.domain.post.dto.PostDto.SinglePostResponse;
import com.prgrms.prolog.domain.post.dto.PostDto.UpdatePostRequest;
import com.prgrms.prolog.domain.post.exception.IllegalAccessPostException;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.posttag.model.PostTag;
import com.prgrms.prolog.domain.posttag.repository.PostTagRepository;
import com.prgrms.prolog.domain.roottag.model.RootTag;
import com.prgrms.prolog.domain.roottag.repository.RootTagRepository;
import com.prgrms.prolog.domain.roottag.util.TagConverter;
import com.prgrms.prolog.domain.series.model.Series;
import com.prgrms.prolog.domain.series.repository.SeriesRepository;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.domain.usertag.model.UserTag;
import com.prgrms.prolog.domain.usertag.repository.UserTagRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final SeriesRepository seriesRepository;
	private final RootTagRepository rootTagRepository;
	private final PostTagRepository postTagRepository;
	private final UserTagRepository userTagRepository;

	@Override
	@Transactional
	public Long createPost(CreatePostRequest createRequest, Long userId) {
		User findUser = userRepository.joinUserTagFindByUserId(userId);
		Post createdPost = CreatePostRequest.from(createRequest, findUser);
		Post savedPost = postRepository.save(createdPost);
		updateNewPostAndUserIfTagExists(createRequest.tagText(), savedPost, findUser);
		registerSeries(createRequest, savedPost, findUser);
		return savedPost.getId();
	}

	private void registerSeries(CreatePostRequest request, Post post, User owner) {
		String seriesTitle = request.seriesTitle();
		if (seriesTitle == null || seriesTitle.isBlank()) {
			seriesTitle = "시리즈 없음";
		}
		final String finalSeriesTitle = seriesTitle;
		Series series = seriesRepository
			.findById(owner.getId())
			.orElseGet(() -> seriesRepository.save(
					Series.builder()
						.title(finalSeriesTitle)
						.user(owner)
						.build()
				)
			);
		post.setSeries(series);
	}

	@Override
	public SinglePostResponse getSinglePost(Long userId, Long postId) {
		Post post = postRepository.joinCommentFindByPostId(postId)
			.orElseThrow(() -> new IllegalArgumentException("exception.post.notExists"));
		if (!post.isOpenStatus() && !post.getUser().checkSameUserId(userId)) { // 비공개 and 다른 사용자인 경우
			throw new IllegalAccessPostException("exception.post.not.access");
		}
		Set<PostTag> findPostTags = postTagRepository.joinRootTagFindByPostId(postId);
		post.addPostTags(findPostTags);
		return SinglePostResponse.from(post);
	}

	@Override
	public Page<SinglePostResponse> getAllPost(Pageable pageable) {
		return postRepository.findAll(pageable)
			.map(SinglePostResponse::from);
	}

	@Override
	@Transactional
	public SinglePostResponse updatePost(UpdatePostRequest updatePostRequest, Long userId, Long postId) {
		Post findPost = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("exception.post.notExists"));

		if (!findPost.getUser().checkSameUserId(userId)) {
			throw new IllegalAccessPostException("exception.post.not.owner");
		}

		findPost.changePost(updatePostRequest);
		updateIfTagChanged(updatePostRequest.tagText(), findPost);

		Set<PostTag> findPostTags = postTagRepository.joinRootTagFindByPostId(findPost.getId());
		findPost.addPostTags(findPostTags);
		return SinglePostResponse.from(findPost);
	}

	@Override
	@Transactional
	public void deletePost(Long userId, Long postId) {
		Post findPost = postRepository.findById(postId)
			.orElseThrow(() -> new IllegalArgumentException("exception.post.notExists"));

		if (!findPost.getUser().checkSameUserId(userId)) {
			throw new IllegalAccessPostException("exception.post.not.owner");
		}

		Set<RootTag> findRootTags = postTagRepository.joinRootTagFindByPostId(findPost.getId())
			.stream()
			.map(PostTag::getRootTag)
			.collect(Collectors.toSet());

		removeOrDecreaseUserTags(findPost.getUser(), findRootTags);
		postTagRepository.deleteByPostId(postId);
		postRepository.delete(findPost);
	}

	private void updateIfTagChanged(String tagText, Post findPost) {
		Set<String> tagNames = TagConverter.convertFrom(tagText);
		Set<RootTag> currentRootTags = rootTagRepository.findByInTagNames(tagNames);
		Set<String> newTagNames = distinguishNewTagNames(tagNames, currentRootTags);
		Set<RootTag> savedNewRootTags = saveNewRootTags(newTagNames);
		saveNewPostTags(findPost, savedNewRootTags);
		saveOrIncreaseUserTags(findPost.getUser(), savedNewRootTags);

		Set<PostTag> findPostTags = postTagRepository.joinRootTagFindByPostId(findPost.getId());
		Set<RootTag> oldRootTags = distinguishOldRootTags(tagNames, findPostTags);
		removeOldPostTags(findPost, oldRootTags);
		removeOrDecreaseUserTags(findPost.getUser(), oldRootTags);
	}

	private void removeOldPostTags(Post post, Set<RootTag> oldRootTags) {
		if (oldRootTags.isEmpty()) {
			return;
		}
		Set<Long> rootTagIds = oldRootTags.stream()
			.map(RootTag::getId)
			.collect(Collectors.toSet());
		postTagRepository.deleteByPostIdAndRootTagIds(post.getId(), rootTagIds);
	}

	private void removeOrDecreaseUserTags(User user, Set<RootTag> oldRootTags) {
		Map<Long, UserTag> userTagMap = getFindUserTagMap(user, oldRootTags);
		for (RootTag rootTag : oldRootTags) {
			if (!userTagMap.containsKey(rootTag.getId())) {
				continue;
			}

			UserTag currentUserTag = userTagMap.get(rootTag.getId());
			currentUserTag.decreaseCount(1);
			if (currentUserTag.isCountZero()) {
				userTagRepository.deleteById(currentUserTag.getId());
			}
		}
	}

	private Set<RootTag> distinguishOldRootTags(Set<String> tagNames, Set<PostTag> postTags) {
		Set<RootTag> oldRootTags = new HashSet<>();
		for (PostTag postTag : postTags) {
			String postTagName = postTag.getRootTag().getName();
			boolean isPostTagRemoved = !tagNames.contains(postTagName);
			if (isPostTagRemoved) {
				oldRootTags.add(postTag.getRootTag());
			}
		}
		return oldRootTags;
	}

	private void updateNewPostAndUserIfTagExists(String tagText, Post savedPost, User findUser) {
		Set<String> tagNames = TagConverter.convertFrom(tagText);
		if (tagNames.isEmpty()) {
			return;
		}

		Set<RootTag> currentRootTags = rootTagRepository.findByInTagNames(tagNames);
		Set<String> newTagNames = distinguishNewTagNames(tagNames, currentRootTags);
		Set<RootTag> savedNewRootTags = saveNewRootTags(newTagNames);
		currentRootTags.addAll(savedNewRootTags);
		saveNewPostTags(savedPost, currentRootTags);
		saveOrIncreaseUserTags(findUser, currentRootTags);
	}

	private void saveOrIncreaseUserTags(User user, Set<RootTag> rootTags) {
		Map<Long, UserTag> userTagMap = getFindUserTagMap(user, rootTags);
		for (RootTag rootTag : rootTags) {
			boolean isUserTagExists = userTagMap.containsKey(rootTag.getId());
			if (isUserTagExists) {
				userTagMap.get(rootTag.getId()).increaseCount(1);
			}
			if (!isUserTagExists) {
				userTagRepository.save(UserTag.builder()
					.user(user)
					.rootTag(rootTag)
					.count(1)
					.build());
			}
		}
	}

	private Map<Long, UserTag> getFindUserTagMap(User user, Set<RootTag> rootTags) {
		List<Long> rootTagIds = rootTags.stream()
			.map(RootTag::getId)
			.toList();
		return userTagRepository.findByUserIdAndInRootTagIds(user.getId(), rootTagIds)
			.stream()
			.collect(Collectors.toMap(userTag -> userTag.getRootTag().getId(), userTag -> userTag));
	}

	private void saveNewPostTags(Post post, Set<RootTag> rootTags) {
		rootTags.forEach(rootTag -> postTagRepository.save(
			PostTag.builder()
				.rootTag(rootTag)
				.post(post)
				.build()));
	}

	private Set<String> distinguishNewTagNames(Set<String> tagNames, Set<RootTag> rootTags) {
		Set<String> newTagNames = new HashSet<>(tagNames);
		for (RootTag rootTag : rootTags) {
			newTagNames.remove(rootTag.getName());
		}
		return newTagNames;
	}

	private Set<RootTag> saveNewRootTags(Set<String> newTagNames) {
		if (newTagNames.isEmpty()) {
			return Collections.emptySet();
		}
		return newTagNames.stream()
			.map(RootTag::new)
			.map(rootTagRepository::save)
			.collect(Collectors.toSet());
	}
}