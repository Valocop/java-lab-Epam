package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

public final class TestUtil {
    private static ModelMapper modelMapper = new ModelMapper();

    public static AuthorDto getTestAuthorDto() {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(RandomStringUtils.random(10, true, false));
        authorDto.setSurname(RandomStringUtils.random(10, true, false));
        return authorDto;
    }

    public static TagDto getTestTagDto() {
        TagDto tagDto = new TagDto();
        tagDto.setName(RandomStringUtils.random(10, true, false));
        return tagDto;
    }

    public static News getTestNews() {
        News news = new News();
        news.setTitle(RandomStringUtils.random(10, true, false));
        news.setFullText(RandomStringUtils.random(10, true, false));
        news.setShortText(RandomStringUtils.random(10, true, false));
        news.setCreationDate(LocalDate.of(2019, 10, 10));
        news.setModificationDate(LocalDate.of(2020, 11, 11));
        return news;
    }

    public static NewsDto getTestNewsDto() {
        NewsDto newsDto = new NewsDto();
        newsDto.setAuthor(getTestAuthorDto());
        newsDto.setTags(new HashSet<>(Arrays.asList(getTestTagDto(), getTestTagDto(), getTestTagDto())));
        newsDto.setTitle(RandomStringUtils.random(10, true, false));
        newsDto.setCreationDate(LocalDate.of(2019, 10, 10));
        newsDto.setModificationDate(LocalDate.of(2020, 11, 11));
        newsDto.setFullText(RandomStringUtils.random(10, true, false));
        newsDto.setShortText(RandomStringUtils.random(10, true, false));
        return newsDto;
    }

    public static AuthorDto convertToDto(Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }

    public static Author convertToEntity(AuthorDto authorDto) {
        return modelMapper.map(authorDto, Author.class);
    }

    public static TagDto convertToDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }

    public static Tag convertToEntity(TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }

    public static NewsDto convertToDto(News news) {
        return modelMapper.map(news, NewsDto.class);
    }

    public static News convertToEntity(NewsDto newsDto) {
        return modelMapper.map(newsDto, News.class);
    }
}
