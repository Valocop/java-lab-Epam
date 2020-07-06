package com.epam.lab.specification;

public class FindNewsByIdSpec implements FindSpecification {
    private String query = "select id, title, short_text, full_text, creation_date, " +
            "modification_date from %s where id = %d";
    private long id;
    private String table = "news";

    public FindNewsByIdSpec(long id, String table) {
        this.id = id;
        this.table = table;
    }

    public FindNewsByIdSpec(long id) {
        this.id = id;
    }

    @Override
    public String query() {
        return String.format(query, table, id);
    }
}
