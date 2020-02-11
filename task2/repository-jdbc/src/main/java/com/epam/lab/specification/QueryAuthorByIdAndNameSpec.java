package com.epam.lab.specification;

public class QueryAuthorByIdAndNameSpec implements QuerySpecification {
    private String query = "select id, name, surname from author where id = %d and name = '%s'";
    private long id;
    private String name;

    public QueryAuthorByIdAndNameSpec(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String query() {
        return String.format(query, id, name);
    }
}
