package com.epam.lab.specification;

public class FindAuthorByIdAndNameSpec implements FindSpecification {
    private String query = "select id, name, surname from author where id = %d and name = '%s'";
    private long id;
    private String name;

    public FindAuthorByIdAndNameSpec(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String query() {
        return String.format(query, id, name);
    }
}
