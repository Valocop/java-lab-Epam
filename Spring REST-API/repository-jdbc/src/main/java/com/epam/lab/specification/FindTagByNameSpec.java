package com.epam.lab.specification;

public class FindTagByNameSpec implements FindSpecification {
    private String query = "select id, name from tag where name = '%s'";
    private String name;

    public FindTagByNameSpec(String name) {
        this.name = name;
    }

    @Override
    public String query() {
        return String.format(query, name);
    }
}
