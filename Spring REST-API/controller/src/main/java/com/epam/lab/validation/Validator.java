package com.epam.lab.validation;

import java.util.List;
import java.util.Map;

public interface Validator {
    ResultValidator validate(Map<String, List<String>> data);
}
