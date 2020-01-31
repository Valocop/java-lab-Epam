package com.epam.lab.repository;

public class QueryTagByIdSpec implements QuerySpecification {
    private String SELECT_BY_ID = "select id, name from %s where id = %d";
    private long id;
    private String table;

    public QueryTagByIdSpec(long id, String table) {
        this.id = id;
        this.table = table;
    }

    @Override
    public String query() {
        return String.format(SELECT_BY_ID, table, id);
    }
}
