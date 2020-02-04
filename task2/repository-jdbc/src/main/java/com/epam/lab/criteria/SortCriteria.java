package com.epam.lab.criteria;

import java.util.List;

public class SortCriteria extends Criteria {

    public SortCriteria(String key, boolean isSort, List<String> values) {
        super(key, isSort, values);
    }
}
