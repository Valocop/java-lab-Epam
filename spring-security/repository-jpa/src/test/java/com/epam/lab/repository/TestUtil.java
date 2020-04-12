package com.epam.lab.repository;

import com.epam.lab.model.*;
import com.epam.lab.specification.NewsSearchSpecification;
import com.epam.lab.specification.NewsSpecificationBuilder;
import com.epam.lab.specification.SearchCriteria;
import com.epam.lab.specification.SearchSpecification;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TestUtil {

    public static News getTestNews() {
        News news = new News();
        news.setTitle(RandomStringUtils.random(10, true, false));
        news.setShortText(RandomStringUtils.random(10, true, false));
        news.setFullText(RandomStringUtils.random(10, true, false));
        news.setCreationDate(LocalDate.of(2019, 10, 10));
        news.setModificationDate(LocalDate.of(2019, 11, 11));
        return news;
    }

    public static Author getTestAuthor() {
        Author author = new Author();
        author.setName(RandomStringUtils.random(10, true, false));
        author.setSurname(RandomStringUtils.random(10, true, false));
        return author;
    }

    public static User getTestUser() {
        User user = new User();
        user.setName(RandomStringUtils.random(10, true, false));
        user.setPassword(RandomStringUtils.random(10, true, false));
        user.setSurname(RandomStringUtils.random(10, true, false));
        user.setUsername(RandomStringUtils.random(10, true, false));
        return user;
    }

    public static Role getTestRole() {
        Role role = new Role();
        role.setName(RandomStringUtils.random(10, true, false));
        return role;
    }

    public static Set<Tag> getTestTags() {
        Set<Tag> tagList = new HashSet<>();
        tagList.add(new Tag(RandomStringUtils.random(5, true, false), null));
        tagList.add(new Tag(RandomStringUtils.random(5, true, false), null));
        return tagList;
    }

    public static Tag getTestTag() {
        Tag tag = new Tag();
        tag.setName(RandomStringUtils.random(5, true, false));
        return tag;
    }

    public static SearchSpecification<News> getTestSpecByAuthorName(String... authorName) {
        List<SearchCriteria> authorNameCriteria = Stream.of(authorName)
                .map(s -> new SearchCriteria(NewsSearchSpecification.AUTHOR_NAME, s))
                .collect(Collectors.toList());
        return new NewsSpecificationBuilder().with(authorNameCriteria).build();
    }
}
