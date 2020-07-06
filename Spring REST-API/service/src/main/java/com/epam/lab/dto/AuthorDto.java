package com.epam.lab.dto;

import com.epam.lab.validation.CreateValidation;
import com.epam.lab.validation.FullValidation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AuthorDto {
    @Min(value = 1,
            message = "id must be above 0",
            groups = FullValidation.class)
    @Max(value = Long.MAX_VALUE, groups = FullValidation.class)
    private long id;
    @NotBlank(message = "name must be not empty",
            groups = {CreateValidation.class, FullValidation.class})
    @Size(message = "name can't be more 30 characters",
            max = 30,
            groups = {CreateValidation.class, FullValidation.class})
    private String name;
    @NotBlank(message = "surname must be not empty",
            groups = {CreateValidation.class, FullValidation.class})
    @Size(message = "surname can't be more 30 characters",
            max = 30,
            groups = {CreateValidation.class, FullValidation.class})
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
