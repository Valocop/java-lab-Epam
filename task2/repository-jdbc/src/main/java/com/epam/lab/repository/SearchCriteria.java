package com.epam.lab.repository;

import java.util.List;

public class SearchCriteria {
    private String key;
    private String operator;
    private List<String> values;
    private SearchType searchType;

    public SearchCriteria(String key, List<String> values) {
        this.key = key;
        this.values = values;
    }



    public enum SearchType {
        TAG_NAME, TAG_ID, AUTHOR_NAME, AUTHOR_SURNAME
    }

    public SearchType getSearchType() {
        switch (key.toLowerCase()) {
            case "tag_id":
                return SearchType.TAG_ID;
            case "tag_name":
                return SearchType.TAG_NAME;
            case "author_name":
                return SearchType.AUTHOR_NAME;
            case "author_surname":
                return SearchType.AUTHOR_SURNAME;
        }
        throw new IllegalStateException("Failed to get search type of params");
    }

    public String getKey() {
        return key;
    }

    public List<String> getValues() {
        return values;
    }
}
