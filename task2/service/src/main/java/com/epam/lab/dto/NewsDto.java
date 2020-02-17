package com.epam.lab.dto;

import com.epam.lab.validation.CreateValidation;
import com.epam.lab.validation.FullValidation;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class NewsDto {
    @Min(value = 1,
            message = "id must be above 0",
            groups = FullValidation.class)
    @Max(value = Long.MAX_VALUE, groups = FullValidation.class)
    private long id;
    @NotBlank(message = "title must be not empty",
            groups = {CreateValidation.class, FullValidation.class})
    @Size(message = "title can't be more 30 characters",
            max = 30,
            groups = {CreateValidation.class, FullValidation.class})
    private String title;
    @NotBlank(message = "short text must be not empty",
            groups = {CreateValidation.class, FullValidation.class})
    @Size(message = "short text can't be more 30 characters",
            max = 100,
            groups = {CreateValidation.class, FullValidation.class})
    private String shortText;
    @NotBlank(message = "full text must be not empty",
            groups = {CreateValidation.class, FullValidation.class})
    @Size(message = "full text can't be more 30 characters",
            max = 2000,
            groups = {CreateValidation.class, FullValidation.class})
    private String fullText;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(groups = {CreateValidation.class, FullValidation.class})
    private LocalDate creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(groups = {CreateValidation.class, FullValidation.class})
    private LocalDate modificationDate;
    @Valid
    private AuthorDto authorDto;
    private List<@Valid TagDto> tagDtoList;

    public NewsDto() {
        creationDate = LocalDate.now();
        modificationDate = LocalDate.now();
    }

    public NewsDto(long id, String title, String shortText, String fullText, LocalDate creationDate, LocalDate modificationDate, AuthorDto authorDto, List<TagDto> tagDtoList) {
        this.id = id;
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.authorDto = authorDto;
        this.tagDtoList = tagDtoList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(LocalDate modificationDate) {
        this.modificationDate = modificationDate;
    }

    public AuthorDto getAuthorDto() {
        return authorDto;
    }

    public void setAuthorDto(AuthorDto authorDto) {
        this.authorDto = authorDto;
    }

    public List<TagDto> getTagDtoList() {
        return tagDtoList;
    }

    public void setTagDtoList(List<TagDto> tagDtoList) {
        this.tagDtoList = tagDtoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsDto newsDto = (NewsDto) o;
        return id == newsDto.id &&
                Objects.equals(title, newsDto.title) &&
                Objects.equals(shortText, newsDto.shortText) &&
                Objects.equals(fullText, newsDto.fullText) &&
                Objects.equals(creationDate, newsDto.creationDate) &&
                Objects.equals(modificationDate, newsDto.modificationDate) &&
                Objects.equals(authorDto, newsDto.authorDto) &&
                Objects.equals(tagDtoList, newsDto.tagDtoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, shortText, fullText, creationDate, modificationDate, authorDto, tagDtoList);
    }
}
