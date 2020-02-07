package com.epam.lab.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

public class TagDto {
    @Min(value = 0, message = "id must be above 0")
    @Max(value = Long.MAX_VALUE, message = "id must be below " + Long.MAX_VALUE)
    private long id;
    @NotEmpty(message = "name must be not empty")
    private String name;

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
