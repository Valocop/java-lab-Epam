package com.epam.lab.specification;

public class QueryNewsByTagIdSpec implements QuerySpecification {
    private static String SELECT_NEWS_BY_TAG = "select id, title, short_text, full_text, creation_date, " +
            "modification_date from news n " +
            "join news_tag nt on n.id = nt.news_id " +
            "where nt.tag_id = %d";
    private long tagId;

    public QueryNewsByTagIdSpec(long tagId) {
        this.tagId = tagId;
    }

    @Override
    public String query() {
        return String.format(SELECT_NEWS_BY_TAG, tagId);
    }
}
