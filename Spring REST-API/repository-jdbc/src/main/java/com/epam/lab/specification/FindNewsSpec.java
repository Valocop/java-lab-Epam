package com.epam.lab.specification;


import com.epam.lab.criteria.Criteria;
import com.epam.lab.criteria.CriteriaType;

public class FindNewsSpec implements FindSpecification {
    private String query = "SELECT news.id, news.title, news.short_text, news.full_text, news.creation_date, news.modification_date " +
            "FROM news " +
            "LEFT JOIN news_tag nt on news.id = nt.news_id " +
            "LEFT JOIN tag t on nt.tag_id = t.id " +
            "LEFT JOIN news_author na on news.id = na.news_id " +
            "LEFT JOIN author a on na.author_id = a.id " +
            "WHERE 1 = 1 ";
    private String inTagById = " and news.id in (select news_id from news_tag where tag_id in (%s) " +
            "group by news_id having count (news_id) = %d) ";
    private String inTagByName = " and news.id in (select news_id from news_tag " +
            "where tag_id in (select id from tag where name in (%s)) " +
            "group by news_id having count (news_id) = %d) ";
    private String andTag = "";
    private String and = "";
    private String order = "";

    @Override
    public String query() {
        return String.format("%s %s %s %s", query, and, andTag,
                (order.trim().isEmpty() ? order : order.substring(0, order.length() - 2)));
    }

    @Override
    public FindSpecification add(Criteria criteria) {
        CriteriaType criteriaType = criteria.getCriteriaType()
                .orElseThrow(() -> new IllegalStateException("Failed to get criteria type"));

        if (criteria.isSort()) {
            return sort(criteria, criteriaType);
        } else {
            return search(criteria, criteriaType);
        }
    }

    private FindSpecification sort(Criteria criteria, CriteriaType criteriaType) {

        switch (criteriaType) {
            case SORT_DATE:
                order(criteria, "news.creation_date");
                return this;
            case SORT_TAG_NAME:
                order(criteria, "t.name");
                return this;
            case SORT_AUTHOR_NAME:
                order(criteria, "a.name");
                return this;
            case SORT_AUTHOR_SURNAME:
                order(criteria, "a.surname");
                return this;
            default:
                return this;
        }
    }

    private FindSpecification search(Criteria criteria, CriteriaType criteriaType) {

        switch (criteriaType) {
            case SEARCH_TAG_ID:
                andTagById(criteria);
                return this;
            case SEARCH_TAG_NAME:
                andTagByName(criteria);
                return this;
            case SEARCH_AUTHOR_NAME:
                andAuthor(criteria, "a.name");
                return this;
            case SEARCH_AUTHOR_SURNAME:
                andAuthor(criteria, "a.surname");
                return this;
            default:
                return this;
        }
    }

    private void andTagById(Criteria criteria) {
        andTag = String.format(inTagById, String.join(",", criteria.getValues()), criteria.getValues().size());
    }

    private void andTagByName(Criteria criteria) {
        andTag = String.format(inTagByName, ("'" + String.join("' , '", criteria.getValues()) + "'"),
                criteria.getValues().size());
    }

    private void andAuthor(Criteria criteria, String field) {
        criteria.getValues().forEach(s -> and += String.format(" and %s = '%s' ", field, s));
    }

    private void order(Criteria criteria, String field) {
        if (order.trim().isEmpty()) {
            order = " order by ";
        }
        order += String.format(" %s %s, ", field,
                (String.join(" ", criteria.getValues()).toLowerCase().contains("desc") ? "DESC" : "ASC"));
    }
}
