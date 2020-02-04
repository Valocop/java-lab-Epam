package com.epam.lab.repository;


import com.epam.lab.criteria.Criteria;

public class QueryNewsSpec implements QuerySpecification {
    private static String IN_TAG = " and n.id in (select news_id from nm.news_tag where %s in (%s) " +
            "group by news_id having count(news_id) = %d) ";
    private String query = "SELECT n.id, n.title, n.short_text, n.full_text, n.creation_date, n.modification_date, " +
            "t.id as tag_id, t.name as tag_name, a.id as author_id, a.name as author_name, a.surname FROM news.news n " +
            "JOIN news.news_tag nt on n.id = nt.news_id " +
            "JOIN news.tag t on nt.tag_id = t.id " +
            "JOIN news.news_author na on n.id = na.news_id " +
            "JOIN news.author a on na.author_id = a.id " +
            "WHERE 1 = 1 " +
            "and a.name = 'Ivan' " +
            "and n.id in (select news_id from nm.news_tag where tag_id in (2,3) " +
            "group by news_id having count(news_id) = 2) " +
            "order by n.creation_date, t.name, a.surname desc";
    private String andTag = "";
    private String and = "";
    private String order = "";

    @Override
    public String query() {
        return query + " " + and + " " + andTag + " " + order;
    }

    @Override
    public QuerySpecification add(Criteria criteria) {
        if (criteria.isSort()) {
            return sort(criteria);
        } else {
            return search(criteria);
        }
    }

    private QuerySpecification sort(Criteria criteria) {
        Criteria.CriteriaType criteriaType = criteria.getCriteriaType()
                .orElseThrow(() -> new IllegalStateException("Failed to get criteria type"));

        switch (criteriaType) {
            case SORT_DATE:

            case SORT_TAG_NAME:

            case SORT_AUTHOR_NAME:

            case SORT_AUTHOR_SURNAME:

            default:
                return this;
        }
    }

    private QuerySpecification search(Criteria criteria) {
        Criteria.CriteriaType criteriaType = criteria.getCriteriaType()
                .orElseThrow(() -> new IllegalStateException("Failed to get criteria type"));

        switch (criteriaType) {
            case SEARCH_TAG_ID:
                andTag(criteria, "tag_id");
            case SEARCH_TAG_NAME:
                andTag(criteria, "tag_name");
            case SEARCH_AUTHOR_NAME:
                andAuthor(criteria, "a.name");
            case SEARCH_AUTHOR_SURNAME:
                andAuthor(criteria, "a.surname");
            default:
                return this;
        }
    }

    private void andTag(Criteria criteria, String where) {
        andTag = String.format(IN_TAG, where, String.join(",", criteria.getValues()),
                criteria.getValues().size());
    }

    private void andAuthor(Criteria criteria, String field) {
        criteria.getValues().forEach(s -> {
            and += String.format(" and %s = '%s' ", field, s);
        });
    }
}
