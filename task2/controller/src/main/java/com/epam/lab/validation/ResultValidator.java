package com.epam.lab.validation;

import java.util.List;
import java.util.Map;

public interface ResultValidator {
    boolean isValid();

    void addException(String key, List<String> exceptions);

    Map<String, List<String>> getExceptionMap();
}
