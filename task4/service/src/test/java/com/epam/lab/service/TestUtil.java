package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.model.Author;
import com.epam.lab.model.Tag;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;

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
}
