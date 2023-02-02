ALTER TABLE users
    change deleted_at deleted tinyint(1) DEFAULT false;
ALTER TABLE post
    change deleted_at deleted boolean DEFAULT false;
ALTER TABLE series
    change deleted_at deleted boolean DEFAULT false;
ALTER TABLE comment
    change deleted_at deleted boolean DEFAULT false;