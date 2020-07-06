package com.epam.lab.validation;

import com.epam.lab.criteria.CriteriaType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SortValidator implements Validator {

    @Override
    public ResultValidator validate(Map<String, List<String>> data) {
        ResultValidator resultValidator = new ResultValidatorImpl();
        data.forEach((s, list) -> {
            Optional<CriteriaType> criteriaType = CriteriaType.getCriteriaType(s, true);
            if (!criteriaType.isPresent()) {
                resultValidator.addException("Sort criteria type",
                        Collections.singletonList("Type of sort incorrect"));
            }
            if (list.isEmpty()) {
                resultValidator.addException("Params of criteria incorrect",
                        Collections.singletonList("Criteria haven't params"));
            }
        });
        return resultValidator;
    }
}
