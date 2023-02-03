drop TABLE social_account;
ALTER TABLE users
    DROP INDEX email;
ALTER TABLE post
    MODIFY COLUMN like_count INT NOT NULL DEFAULT 0;

