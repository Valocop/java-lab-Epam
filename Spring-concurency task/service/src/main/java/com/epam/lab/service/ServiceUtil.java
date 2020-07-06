package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.dto.UserDto;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.model.User;
import org.modelmapper.ModelMapper;

public final class ServiceUtil {
    private static ModelMapper modelMapper = new ModelMapper();

    public static TagDto convertToDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }

    public static Tag convertToEntity(TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }

    public static AuthorDto convertToDto(Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }

    public static Author convertToEntity(AuthorDto authorDto) {
        return modelMapper.map(authorDto, Author.class);
    }

    public static NewsDto convertToDto(News news) {
        return modelMapper.map(news, NewsDto.class);
    }

    public static News convertToEntity(NewsDto newsDto) {
        return modelMapper.map(newsDto, News.class);
    }

    public static UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public static User convertToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
//    public static <D, E> D convertToDto(E entity, D dto) {
//        return modelMapper.map(entity, (Type) dto.getClass());
//    }
}
