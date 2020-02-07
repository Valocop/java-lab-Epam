
CREATE TABLE "user"
(
    id       bigint                NOT NULL AUTO_INCREMENT,
    name     character varying(20) NOT NULL,
    surname  character varying(20) NOT NULL,
    login    character varying(30) NOT NULL,
    password character varying(30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE roles
(
    user_id   bigint                NOT NULL,
    role_name character varying(30) NOT NULL,
    foreign key (user_id) references "user" (id)
);

CREATE TABLE author
(
    id      bigint                NOT NULL AUTO_INCREMENT,
    name    character varying(30) NOT NULL,
    surname character varying(30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE news
(
    id                bigint                  NOT NULL AUTO_INCREMENT,
    title             character varying(30)   NOT NULL,
    short_text        character varying(100)  NOT NULL,
    full_text         character varying(2000) NOT NULL,
    creation_date     date                    NOT NULL,
    modification_date date                    NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tag
(
    id   bigint                NOT NULL AUTO_INCREMENT,
    name character varying(30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE news_author
(
    news_id   bigint NOT NULL,
    author_id bigint NOT NULL,
    foreign key (news_id) references news (id),
    foreign key (author_id) references author (id)
);

CREATE TABLE news_tag
(
    news_id bigint NOT NULL,
    tag_id  bigint NOT NULL,
    foreign key (news_id) references news (id),
    foreign key (tag_id) references tag (id)
);
