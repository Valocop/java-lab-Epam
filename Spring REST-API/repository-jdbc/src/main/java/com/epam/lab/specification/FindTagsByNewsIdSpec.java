package com.epam.lab.specification;

public class FindTagsByNewsIdSpec implements FindSpecification {
    private long newsId;

    public FindTagsByNewsIdSpec(long newsId) {
        this.newsId = newsId;
    }

    @Override
    public String query() {
        return String.format("select id, name from tag t " +
                "join news_tag nt on t.id = nt.tag_id " +
                "where nt.news_id = %d", newsId);
    }
}
