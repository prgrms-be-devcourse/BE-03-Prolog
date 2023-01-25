DROP TABLE IF EXISTS user_tag;
DROP TABLE IF EXISTS post_tag;
DROP TABLE IF EXISTS root_tag;

CREATE TABLE root_tag
(
    id   bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) NOT NULL UNIQUE
);

CREATE TABLE post_tag
(
    id          bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    post_id     bigint NOT NULL,
    root_tag_id bigint NOT NULL,
    FOREIGN KEY fk_post_tag_post_id (post_id) REFERENCES post (id),
    FOREIGN KEY fk_post_tag_root_tag_id (root_tag_id) REFERENCES root_tag (id)
);

CREATE TABLE user_tag
(
    id          bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    count       int    NOT NULL default 0,
    user_id     bigint NOT NULL,
    root_tag_id bigint NOT NULL,
    FOREIGN KEY fk_user_tag_user_id (user_id) REFERENCES users (id),
    FOREIGN KEY fk_user_tag_root_tag_id (root_tag_id) REFERENCES root_tag (id)
)