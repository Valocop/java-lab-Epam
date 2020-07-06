package com.epam.lab.specification;

public class FindNewsByTagIdSpec implements FindSpecification {
    private long tagId;

    public FindNewsByTagIdSpec(long tagId) {
        this.tagId = tagId;
    }

    @Override
    public String query() {
        return String.format("select id, title, short_text, full_text, creation_date, " +
                "modification_date from news n " +
                "join news_tag nt on n.id = nt.news_id " +
                "where nt.tag_id = %d", tagId);
    }
}
