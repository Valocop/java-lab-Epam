package com.epam.lab.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultValidatorImpl implements ResultValidator {
    private static final Logger log = LogManager.getLogger();
    private Map<String, List<String>> exceptionMap = new HashMap<>();

    @Override
    public boolean isValid() {
        if (exceptionMap.isEmpty()) {
            log.info("Result of validation is valid");
            return true;
        } else {
            log.info("Result of validation is not valid");
            return false;
        }
    }

    @Override
    public void addException(String key, List<String> exceptions) {
        if (exceptionMap.containsKey(key)) {
            exceptionMap.get(key).addAll(exceptions);
        } else {
            exceptionMap.put(key, exceptions);
        }
    }

    @Override
    public Map<String, List<String>> getExceptionMap() {
        return exceptionMap;
    }
}
