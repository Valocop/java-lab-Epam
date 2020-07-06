package com.epam.lab.specification;

import com.epam.lab.exception.IncorrectRequestException;
import com.epam.lab.model.News;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public class NewsSortSpecification implements SortSpecification<News> {
    public static final String AUTHOR_NAME = "author_name";
    public static final String CREATION_DATE = "creation_date";
    public static final String CREATION = "creationDate";
    private SortCriteria sortCriteria;

    public NewsSortSpecification(SortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    @Override
    public Order toOrder(Root<News> root, CriteriaQuery<News> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        switch (sortCriteria.getKey()) {
            case CREATION_DATE:
                return criteriaBuilder.desc(root.get(CREATION));
            case AUTHOR_NAME:
                String[] fieldKeys = sortCriteria.getKey().split("_");
                return criteriaBuilder.asc(root.join(fieldKeys[0]).get(fieldKeys[1]));
            default:
                throw new IncorrectRequestException("Incorrect data for sorting!");
        }
    }
}
