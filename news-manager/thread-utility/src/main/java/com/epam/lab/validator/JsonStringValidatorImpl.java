package com.epam.lab.validator;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.validation.CreatingValidation;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

public class JsonStringValidatorImpl implements JsonStringValidator {
    private Validator validator;

    public JsonStringValidatorImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public boolean validate(List<NewsDto> newsDtoList) {
        for (NewsDto newsDto : newsDtoList) {
            Set<ConstraintViolation<NewsDto>> constraintViolations = validator.validate(newsDto, CreatingValidation.class);
            if (isNotValidConstrains(constraintViolations)) {
                return false;
            }
        }
        return true;
    }

    private boolean isNotValidConstrains(Set<ConstraintViolation<NewsDto>> constraintViolations) {
        return !constraintViolations.isEmpty();
    }
}
