package com.epam.lab.specification;

public class QueryTagByNameSpec implements QuerySpecification {
    private String query = "select id, name from tag where name = '%s'";
    private String name;

    public QueryTagByNameSpec(String name) {
        this.name = name;
    }

    @Override
    public String query() {
        return String.format(query, name);
    }
}
