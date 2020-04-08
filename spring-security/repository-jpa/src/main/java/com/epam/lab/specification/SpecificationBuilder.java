package com.epam.lab.specification;

import java.util.List;

public interface SpecificationBuilder<T> {

    SpecificationBuilder<T> with(SearchCriteria searchCriteria);

    SpecificationBuilder<T> with(List<SearchCriteria> criteriaList);

    SearchSpecification<T> build();
}
