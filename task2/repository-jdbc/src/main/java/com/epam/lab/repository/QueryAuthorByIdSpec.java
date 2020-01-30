package com.epam.lab.repository;

public class QueryAuthorByIdSpec implements QuerySpecification {
    private String SELECT_BY_ID = "select id, name, surname from %s where id = %d";
    private final long id;
    private final String table;

    public QueryAuthorByIdSpec(long id, String table) {
        this.id = id;
        this.table = table;
    }

    @Override
    public String query() {
        return String.format(SELECT_BY_ID, table, id);
    }
}
