package com.epam.lab.dto;

import com.epam.lab.validation.CreatingValidation;
import com.epam.lab.validation.UpdatingValidation;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class NewsDto {
    @Min(value = 1,
            groups = UpdatingValidation.class,
            message = "id must be greater than 1")
    @Max(value = Long.MAX_VALUE,
            groups = UpdatingValidation.class,
            message = "id must be less than " + Long.MAX_VALUE)
    private long id;
    @NotBlank(groups = {CreatingValidation.class, UpdatingValidation.class},
            message = "title must not be empty")
    @Size(max = 30,
            groups = {CreatingValidation.class, UpdatingValidation.class},
            message = "title must be less than 30 symbols")
    private String title;
    @NotBlank(groups = {CreatingValidation.class, UpdatingValidation.class},
            message = "shortText must not be empty")
    @Size(max = 100,
            groups = {CreatingValidation.class, UpdatingValidation.class},
            message = "shortText must be less than 100 symbols")
    private String shortText;
    @NotBlank(groups = {CreatingValidation.class, UpdatingValidation.class},
            message = "fullText must not be empty")
    @Size(max = 2000,
            groups = {CreatingValidation.class, UpdatingValidation.class},
            message = "fullText must be less than 100 symbols")
    private String fullText;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate modificationDate;
    @NotNull(groups = {CreatingValidation.class, UpdatingValidation.class},
            message = "author field must be")
    @Valid
    private AuthorDto author;
    @NotNull(groups = {CreatingValidation.class, UpdatingValidation.class},
            message = "tags field must be")
    private Set<@Valid TagDto> tags;

    public NewsDto() {
        creationDate = LocalDate.now();
        modificationDate = LocalDate.now();
        author = new AuthorDto();
        tags = new HashSet<>();
    }

    public NewsDto(String title, String shortText, String fullText, LocalDate creationDate,
                   LocalDate modificationDate, AuthorDto author, Set<TagDto> tags) {
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.author = author;
        this.tags = tags;
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

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = tags;
    }
}
