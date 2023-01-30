-- init.sql
# create database if not exists prolog;
# use prolog;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS user_tag;
DROP TABLE IF EXISTS post_tag;
DROP TABLE IF EXISTS root_tag;
DROP TABLE IF EXISTS social_account;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS series;
DROP TABLE IF EXISTS users;


CREATE TABLE users
(
    id              bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email           varchar(100) NOT NULL UNIQUE,
    profile_img_url varchar(255) NULL,
    nick_name       varchar(100) NULL UNIQUE,
    introduce       varchar(100) NULL,
    prolog_name     varchar(100) NOT NULL UNIQUE,
    provider        varchar(100) NOT NULL,
    oauth_id        varchar(100) NOT NULL,
    created_by      varchar(100) NULL,
    created_at      datetime     NOT NULL DEFAULT now(),
    updated_at      datetime     NOT NULL DEFAULT now(),
    deleted_at      datetime
);

CREATE TABLE series
(
    id         bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title      varchar(200) NOT NULL,
    created_at datetime     NOT NULL DEFAULT now(),
    updated_at datetime     NOT NULL DEFAULT now(),
    deleted_at datetime,
    user_id    bigint       NOT NULL,
    FOREIGN KEY fk_series_user_id (user_id) REFERENCES users (id)
);

CREATE TABLE post
(
    id          bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title       varchar(200) NOT NULL,
    content     text         NOT NULL,
    open_status tinyint(1)   NOT NULL DEFAULT 0,
    created_by  varchar(100) NULL,
    created_at  datetime     NOT NULL DEFAULT now(),
    updated_at  datetime     NOT NULL DEFAULT now(),
    deleted_at  datetime,
    user_id     bigint       NOT NULL,
    series_id   bigint       NULL,
    FOREIGN KEY fk_post_user_id (user_id) REFERENCES users (id),
    FOREIGN KEY fk_post_series_id (series_id) REFERENCES series (id)
);

CREATE TABLE social_account
(
    id          bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email       varchar(100),
    facebook_id varchar(100),
    github_id   varchar(100),
    twitter_id  varchar(100),
    blog_url    varchar(100),
    created_by  varchar(100) NULL,
    created_at  datetime     NOT NULL DEFAULT now(),
    updated_at  datetime     NOT NULL DEFAULT now(),
    deleted_at  datetime,
    user_id     bigint       NOT NULL,
    FOREIGN KEY fk_social_account_user_id (user_id) REFERENCES users (id)
);

CREATE TABLE comment
(
    id         bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    content    varchar(255) NOT NULL,
    created_by varchar(100) NULL,
    created_at datetime     NOT NULL DEFAULT now(),
    updated_at datetime     NOT NULL DEFAULT now(),
    deleted_at datetime,
    post_id    bigint       NOT NULL,
    user_id    bigint       NOT NULL,
    FOREIGN KEY fk_comment_post_id (post_id) REFERENCES post (id),
    FOREIGN KEY fk_comment_user_id (user_id) REFERENCES users (id)
);

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
);

CREATE TABLE likes
(
    id      bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id bigint NOT NULL,
    post_id bigint NOT NULL,
    FOREIGN KEY fk_likes_user_id (user_id) REFERENCES users (id),
    FOREIGN KEY fk_likes_post_id (post_id) REFERENCES post (id)
);

ALTER TABLE post
    ADD like_count INT DEFAULT 0;