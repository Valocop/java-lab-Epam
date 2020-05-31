package com.epam.lab.dto;

import com.epam.lab.validation.CreatingValidation;
import com.epam.lab.validation.UpdatingValidation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TagDto {
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

    public TagDto() {
    }

    public TagDto(long id, String name) {
        this.id = id;
        this.name = name;
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
}
