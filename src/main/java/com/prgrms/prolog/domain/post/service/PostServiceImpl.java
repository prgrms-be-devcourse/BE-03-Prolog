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

import com.prgrms.prolog.domain.post.dto.PostRequest.CreateRequest;
import com.prgrms.prolog.domain.post.dto.PostRequest.UpdateRequest;
import com.prgrms.prolog.domain.post.dto.PostResponse;
import com.prgrms.prolog.domain.post.model.Post;
import com.prgrms.prolog.domain.post.repository.PostRepository;
import com.prgrms.prolog.domain.posttag.model.PostTag;
import com.prgrms.prolog.domain.posttag.repository.PostTagRepository;
import com.prgrms.prolog.domain.roottag.model.RootTag;
import com.prgrms.prolog.domain.roottag.repository.RootTagRepository;
import com.prgrms.prolog.domain.roottag.util.TagConverter;
import com.prgrms.prolog.domain.user.model.User;
import com.prgrms.prolog.domain.user.repository.UserRepository;
import com.prgrms.prolog.domain.usertag.model.UserTag;
import com.prgrms.prolog.domain.usertag.repository.UserTagRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private static final String POST_NOT_EXIST_MESSAGE = "존재하지 않는 게시물입니다.";
	private static final String USER_NOT_EXIST_MESSAGE = "존재하지 않는 사용자입니다.";

	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final RootTagRepository rootTagRepository;
	private final PostTagRepository postTagRepository;
	private final UserTagRepository userTagRepository;

	@Override
	@Transactional
	public Long save(CreateRequest request, Long userId) {
		User findUser = userRepository.joinUserTagFindByUserId(userId);
		Post createdPost = CreateRequest.toEntity(request, findUser);
		Post savedPost = postRepository.save(createdPost);
		updateNewPostAndUserIfTagExists(request.tagText(), savedPost, findUser);
		return savedPost.getId();
	}

	@Override
	public PostResponse findById(Long postId) {
		Post post = postRepository.joinCommentFindById(postId)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST_MESSAGE));
		List<PostTag> findPostTags = postTagRepository.joinPostTagFindByPostId(postId);
		post.addPostTagsFrom(findPostTags);
		return PostResponse.toPostResponse(post);
	}

	@Override
	public Page<PostResponse> findAll(Pageable pageable) {
		return postRepository.findAll(pageable)
			.map(PostResponse::toPostResponse);
	}

	@Override
	@Transactional
	public PostResponse update(UpdateRequest update, Long userId, Long postId) {
		Post findPost = postRepository.joinUserFindById(postId)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST_MESSAGE));
		
		if (!findPost.getUser().checkSameUserId(userId)) {
			throw new IllegalArgumentException("exception.post.not.owner"); 
		}

		findPost.changePost(update);
		updatePostAndUserIfTagChanged(update.tagText(), findPost);
		return PostResponse.toPostResponse(findPost);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		Post findPost = postRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException(POST_NOT_EXIST_MESSAGE));
		postRepository.delete(findPost);
	}

	private void updatePostAndUserIfTagChanged(String tagText, Post findPost) {
		Set<String> tagNames = TagConverter.convertFrom(tagText);
		Set<RootTag> currentRootTags = rootTagRepository.findByTagNamesIn(tagNames);
		Set<String> newTagNames = distinguishNewTagNames(tagNames, currentRootTags);
		Set<RootTag> oldRootTags = distinguishOldRootTags(tagNames, findPost.getPostTags());
		Set<RootTag> savedNewRootTags = saveNewRootTags(newTagNames);
		currentRootTags.addAll(savedNewRootTags);

		removeOldPostTags(findPost, oldRootTags);
		saveNewPostTags(findPost, savedNewRootTags);
		removeOrDecreaseUserTags(findPost.getUser(), oldRootTags);
	}

	private void removeOldPostTags(Post post, Set<RootTag> oldRootTags) {
		if (oldRootTags.isEmpty()) {
			return;
		}
		List<Long> rootTagIds = oldRootTags.stream()
			.map(RootTag::getId)
			.toList();
		postTagRepository.deleteByPostIdAndRootTagIds(post.getId(), rootTagIds);
	}

	private void removeOrDecreaseUserTags(User user, Set<RootTag> oldRootTags) {
		Map<Long, UserTag> userTagMap = getFindUserTagMap(user, oldRootTags);
		for (RootTag rootTag : oldRootTags) {
			if (userTagMap.containsKey(rootTag.getId())) {
				userTagMap.get(rootTag.getId()).decreaseCount(1);
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

		Set<RootTag> currentRootTags = rootTagRepository.findByTagNamesIn(tagNames);
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

	private Set<String> distinguishNewTagNames(Set<String> newTagNames, Set<RootTag> rootTags) {
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