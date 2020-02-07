package com.epam.lab.specification;

public class QueryAuthorByIdSpec implements QuerySpecification {
    private String SELECT_BY_ID = "select id, name, surname from %s where id = %d";
    private long id;
    private String table = "author";

    public QueryAuthorByIdSpec(long id) {
        this.id = id;
    }

    public QueryAuthorByIdSpec(long id, String table) {
        this.id = id;
        this.table = table;
    }

    @Override
    public String query() {
        return String.format(SELECT_BY_ID, table, id);
    }
}
