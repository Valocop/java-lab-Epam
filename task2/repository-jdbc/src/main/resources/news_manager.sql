-- PostgreSQL 10.11

-- Create schema news
CREATE SCHEMA news

-- Create table author
CREATE TABLE news.author
(
    id bigint NOT NULL,
    name character varying(30) NOT NULL,
    surname character varying(30) NOT NULL,
    PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
);

ALTER TABLE news.author
    OWNER to secure;

-- Create table news
CREATE TABLE news.news
(
    id bigint NOT NULL,
    title character varying(30) NOT NULL,
    short_text character varying(100) NOT NULL,
    full_text character varying(2000) NOT NULL,
    creation_date date NOT NULL,
    modification_date date NOT NULL,
    PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
);

ALTER TABLE news.news
    OWNER to secure;

-- Create table news_author
CREATE TABLE news.news_author
(
    news_id bigint NOT NULL,
    author_id bigint NOT NULL,
    CONSTRAINT news_id FOREIGN KEY (news_id)
        REFERENCES news.news (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT author_id FOREIGN KEY (author_id)
        REFERENCES news.author (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
);

ALTER TABLE news.news_author
    OWNER to secure;

-- Create table tag
CREATE TABLE news.tag
(
    id bigint NOT NULL,
    name character varying(30) NOT NULL,
    PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
);

ALTER TABLE news.tag
    OWNER to secure;

-- Create table news_tag
CREATE TABLE news.news_tag
(
    news_id bigint NOT NULL,
    tag_id bigint NOT NULL,
    CONSTRAINT news_id FOREIGN KEY (news_id)
        REFERENCES news.news (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT tag_id FOREIGN KEY (tag_id)
        REFERENCES news.tag (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
);

ALTER TABLE news.news_tag
    OWNER to secure;

-- Create table user
CREATE TABLE news."user"
(
    id bigint NOT NULL,
    name character varying(20) NOT NULL,
    surname character varying(20) NOT NULL,
    login character varying(30) NOT NULL,
    password character varying(30) NOT NULL,
    PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
);

ALTER TABLE news."user"
    OWNER to secure;

-- Create table roles
CREATE TABLE news.roles
(
    user_id bigint NOT NULL,
    role_name character varying(30) NOT NULL,
    CONSTRAINT user_id FOREIGN KEY (user_id)
        REFERENCES news."user" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)
WITH (
    OIDS = FALSE
);

ALTER TABLE news.roles
    OWNER to secure;