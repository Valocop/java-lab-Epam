package com.epam.lab.repository;

import com.epam.lab.criteria.Criteria;

public interface QuerySpecification {
    String query();

    default QuerySpecification add(Criteria criteria) {
        return () -> query() + String.format(" and %s = %s ", criteria.getKey(), criteria.getValues());
    }
}
