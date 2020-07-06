package com.epam.lab.specification;

public class FindNewsByAuthorIdSpec implements FindSpecification {
    private long authorId;

    public FindNewsByAuthorIdSpec(long authorId) {
        this.authorId = authorId;
    }

    @Override
    public String query() {
        return String.format("select id, title, short_text, full_text, creation_date, " +
                "modification_date from news n " +
                "join news_author na on n.id = na.news_id " +
                "where na.author_id = %d", authorId);
    }
}
