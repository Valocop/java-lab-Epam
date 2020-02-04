package com.epam.lab.criteria;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class Criteria {
    private String key;
    private boolean isSort = false;
    private List<String> values;

    public Criteria(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }

    public Criteria(String key, boolean isSort, List<String> values) {
        this.key = key;
        this.isSort = isSort;
        this.values = values;
    }

    public Optional<CriteriaType> getCriteriaType() {
        return CriteriaType.getCriteriaType(key, isSort);
    }

    public enum CriteriaType {
        SEARCH_TAG_NAME, SEARCH_TAG_ID, SEARCH_AUTHOR_NAME, SEARCH_AUTHOR_SURNAME,
        SORT_DATE, SORT_AUTHOR_NAME, SORT_AUTHOR_SURNAME, SORT_TAG_NAME;

        private static Optional<CriteriaType> getCriteriaType(String key, boolean isSort) {
            String type = isSort ? "sort_" : "search_";
            return Stream.of(CriteriaType.values())
                    .filter(criteriaType -> criteriaType.name().equalsIgnoreCase(type + key))
                    .findFirst();
        }
    }

    public String getKey() {
        return key;
    }

    public boolean isSort() {
        return isSort;
    }

    public List<String> getValues() {
        return values;
    }
}
