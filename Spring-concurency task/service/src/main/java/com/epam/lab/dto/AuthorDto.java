package com.epam.lab.dto;

import com.epam.lab.validation.CreatingValidation;
import com.epam.lab.validation.UpdatingValidation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AuthorDto {
    @Min(value = 0,
            groups = UpdatingValidation.class,
            message = "id must be positive")
    @Max(value = Long.MAX_VALUE,
            groups = UpdatingValidation.class,
            message = "id must be less than " + Long.MAX_VALUE)
    private long id;
    @NotBlank(groups = {CreatingValidation.class, UpdatingValidation.class},
            message = "name must not be empty")
    @Size(max = 30,
            groups = {CreatingValidation.class, UpdatingValidation.class},
            message = "name must be less than 30 symbols")
    private String name;
    @NotBlank(groups = {CreatingValidation.class, UpdatingValidation.class},
            message = "surname must not be empty")
    @Size(max = 30,
            groups = {CreatingValidation.class, UpdatingValidation.class},
            message = "surname must be less than 30 symbols")
    private String surname;

    public AuthorDto() {
    }

    public AuthorDto(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
