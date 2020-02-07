package com.epam.lab.specification;

public class QueryTagsByNewsIdSpec implements QuerySpecification {
    private static String SELECT_TAGS_BY_NEWS = "select id, name from tag t " +
            "join news_tag nt on t.id = nt.tag_id " +
            "where nt.news_id = %d";
    private long newsId;

    public QueryTagsByNewsIdSpec(long newsId) {
        this.newsId = newsId;
    }

    @Override
    public String query() {
        return String.format(SELECT_TAGS_BY_NEWS, newsId);
    }
}
