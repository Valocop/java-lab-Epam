package com.epam.lab.specification;

public class FindTagByIdSpec implements FindSpecification {
    private long id;
    private String table = "tag";

    public FindTagByIdSpec(long id) {
        this.id = id;
    }

    public FindTagByIdSpec(long id, String table) {
        this.id = id;
        this.table = table;
    }

    @Override
    public String query() {
        return String.format("select id, name from %s where id = %d", table, id);
    }
}
