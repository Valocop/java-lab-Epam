package com.epam.lab.specification;

import com.epam.lab.criteria.Criteria;

public interface FindSpecification {
    String query();

    default FindSpecification add(Criteria criteria) {
        return () -> query() + String.format(" and %s = %s ", criteria.getKey(), criteria.getValues());
    }
}
