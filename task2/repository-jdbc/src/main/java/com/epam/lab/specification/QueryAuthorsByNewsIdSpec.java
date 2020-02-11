package com.epam.lab.specification;

public class QueryAuthorsByNewsIdSpec implements QuerySpecification {
    private static String SELECT_AUTHORS_BY_NEWS = "select id, name, surname from author a " +
            "join news_author na on a.id = na.author_id " +
            "where na.news_id = %d";
    private long newsId;

    public QueryAuthorsByNewsIdSpec(long newsId) {
        this.newsId = newsId;
    }

    @Override
    public String query() {
        return String.format(SELECT_AUTHORS_BY_NEWS, newsId);
    }
}
