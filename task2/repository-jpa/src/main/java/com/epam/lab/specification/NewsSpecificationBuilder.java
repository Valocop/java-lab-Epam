package com.epam.lab.specification;

import com.epam.lab.model.News;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewsSpecificationBuilder implements SpecificationBuilder<News> {
    private final List<SearchCriteria> criteriaList;

    public NewsSpecificationBuilder() {
        this.criteriaList = new ArrayList<>();
    }

    @Override
    public SpecificationBuilder<News> with(SearchCriteria searchCriteria) {
        criteriaList.add(searchCriteria);
        return this;
    }

    @Override
    public SpecificationBuilder<News> with(List<SearchCriteria> criteriaList) {
        this.criteriaList.addAll(criteriaList);
        return this;
    }

    @Override
    public SearchSpecification<News> build() {
        List<NewsSearchSpecification> specificationList = criteriaList.stream()
                .map(NewsSearchSpecification::new)
                .collect(Collectors.toList());
        SearchSpecification<News> searchSpecification = specificationList.get(0);
        for (int i = 0; i < criteriaList.size(); i++) {
            searchSpecification = searchSpecification.and(specificationList.get(i));
        }
        return searchSpecification;
    }
}
