package com.epam.lab.specification;

public class QueryNewsByAuthorIdSpec implements QuerySpecification {
    private static String SELECT_NEWS_BY_AUTHOR = "select id, title, short_text, full_text, creation_date, " +
            "modification_date from news n " +
            "join news_author na on n.id = na.news_id " +
            "where na.author_id = %d";
    private long authorId;

    public QueryNewsByAuthorIdSpec(long authorId) {
        this.authorId = authorId;
    }

    @Override
    public String query() {
        return String.format(SELECT_NEWS_BY_AUTHOR, authorId);
    }
}
