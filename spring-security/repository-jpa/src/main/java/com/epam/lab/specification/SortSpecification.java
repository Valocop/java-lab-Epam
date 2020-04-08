package com.epam.lab.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public interface SortSpecification<T> {

    Order toOrder(Root<T> root, CriteriaQuery<T> criteriaQuery, CriteriaBuilder criteriaBuilder);
}
