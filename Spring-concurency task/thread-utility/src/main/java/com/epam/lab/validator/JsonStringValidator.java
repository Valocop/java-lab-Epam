package com.epam.lab.validator;

import com.epam.lab.dto.NewsDto;

import java.util.List;

public interface JsonStringValidator {
    boolean validate(List<NewsDto> newsDtoList);
}
