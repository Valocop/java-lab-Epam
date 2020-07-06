package com.epam.lab.specification;

public class FindAuthorByIdSpec implements FindSpecification {
    private long id;
    private String table = "author";

    public FindAuthorByIdSpec(long id) {
        this.id = id;
    }

    public FindAuthorByIdSpec(long id, String table) {
        this.id = id;
        this.table = table;
    }

    @Override
    public String query() {
        return String.format("select id, name, surname from %s where id = %d", table, id);
    }
}
