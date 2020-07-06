package com.epam.lab.criteria;

import java.util.List;
import java.util.Optional;

public class Criteria {
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
