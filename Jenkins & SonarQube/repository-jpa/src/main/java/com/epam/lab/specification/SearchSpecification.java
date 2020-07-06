package com.epam.lab.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

import static com.epam.lab.specification.SpecificationComposition.composed;

public interface SearchSpecification<T> extends Serializable {

    long serialVersionUID = 1L;

    default SearchSpecification<T> and(SearchSpecification<T> other) {
        return composed(this, other, CriteriaBuilder::and);
    }

    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);
}
