package com.epam.lab.repository;

public class QueryNewsByIdSpec implements QuerySpecification{
    private String SELECT_BY_ID = "select id, title, short_text, full_text, creation_date, " +
            "modification_date from %s where id = %d";
    private long id;
    private String table;

    public QueryNewsByIdSpec(long id, String table) {
        this.id = id;
        this.table = table;
    }

    @Override
    public String query() {
        return String.format(SELECT_BY_ID, table, id);
    }
}
