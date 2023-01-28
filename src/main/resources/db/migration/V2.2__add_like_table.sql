DROP TABLE IF EXISTS likes;

CREATE TABLE likes
(
    id      bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id bigint NOT NULL,
    post_id bigint NOT NULL,
    FOREIGN KEY fk_likes_user_id (user_id) REFERENCES users (id),
    FOREIGN KEY fk_likes_post_id (post_id) REFERENCES post (id)
);

ALTER TABLE post ADD like_count INT DEFAULT 0;