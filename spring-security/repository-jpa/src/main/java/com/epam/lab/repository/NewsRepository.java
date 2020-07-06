package com.epam.lab.repository;

import com.epam.lab.model.News;
import com.epam.lab.specification.SearchSpecification;
import com.epam.lab.specification.SortSpecification;

import java.util.List;

public interface NewsRepository extends CrudRepository<News, Long> {

    List<News> findAll(SearchSpecification<News> searchSpec);

    List<News> findAll(SortSpecification<News> sortSpec, Integer count, Integer page);

    List<News> findAll(SearchSpecification<News> searchSpec, SortSpecification<News> sortSpec,
                       Integer count, Integer page);

    long count(SearchSpecification<News> searchSpec);

    long count();
}
