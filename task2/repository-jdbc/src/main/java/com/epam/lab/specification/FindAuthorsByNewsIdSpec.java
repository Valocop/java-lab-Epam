package com.epam.lab.specification;

public class FindAuthorsByNewsIdSpec implements FindSpecification {
    private long newsId;

    public FindAuthorsByNewsIdSpec(long newsId) {
        this.newsId = newsId;
    }

    @Override
    public String query() {
        return String.format("select id, name, surname from author a " +
                "join news_author na on a.id = na.author_id " +
                "where na.news_id = %d", newsId);
    }
}
