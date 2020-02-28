package com.epam.lab.specification;

import com.epam.lab.exception.IncorrectRequestException;
import com.epam.lab.model.News;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NewsSearchSpecification implements SearchSpecification<News> {
    public static final String AUTHOR_ID = "author_id";
    public static final String NEWS_ID = "news_id";
    public static final String AUTHOR_NAME = "author_name";
    public static final String TAGS_NAME = "tags_name";
    private SearchCriteria searchCriteria;

    public NewsSearchSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<News> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        switch (searchCriteria.getKey()) {
            case NEWS_ID:
                return criteriaBuilder.equal(root.get("id"), searchCriteria.getValue());
            case AUTHOR_NAME:
            case AUTHOR_ID:
            case TAGS_NAME:
                String[] fieldKeys = searchCriteria.getKey().split("_");
                return criteriaBuilder.equal(root.join(fieldKeys[0]).get(fieldKeys[1]), searchCriteria.getValue());
            default:
                throw new IncorrectRequestException("Incorrect data for search");
        }
    }
}
