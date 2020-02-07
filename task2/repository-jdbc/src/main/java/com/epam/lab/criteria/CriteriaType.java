package com.epam.lab.criteria;

import java.util.Optional;
import java.util.stream.Stream;

public enum CriteriaType {
    SEARCH_TAG_NAME, SEARCH_TAG_ID, SEARCH_AUTHOR_NAME, SEARCH_AUTHOR_SURNAME,
    SORT_DATE, SORT_AUTHOR_NAME, SORT_AUTHOR_SURNAME, SORT_TAG_NAME;

    public static Optional<CriteriaType> getCriteriaType(String key, boolean isSort) {
        String type = isSort ? "sort_" : "search_";
        return Stream.of(CriteriaType.values())
                .filter(criteriaType -> criteriaType.name().equalsIgnoreCase(type + key))
                .findFirst();
    }
}
