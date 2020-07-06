package com.epam.lab.dto;

import com.epam.lab.validation.CreateValidation;
import com.epam.lab.validation.FullValidation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TagDto {
    @Min(value = 1, groups = FullValidation.class)
    @Max(value = Long.MAX_VALUE, groups = FullValidation.class)
    private long id;
    @NotBlank(message = "name must be not empty",
            groups = {CreateValidation.class, FullValidation.class})
    @Size(message = "name can't be more 30 characters",
            max = 30,
            groups = {CreateValidation.class, FullValidation.class})
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
