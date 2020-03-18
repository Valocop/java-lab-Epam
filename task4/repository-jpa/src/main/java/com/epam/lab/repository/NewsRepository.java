package com.epam.lab.repository;

import com.epam.lab.model.News;
import com.epam.lab.specification.SearchSpecification;
import com.epam.lab.specification.SortSpecification;

import java.util.List;

public interface NewsRepository extends CrudRepository<News, Long> {

    List<News> findAll(SearchSpecification<News> searchSpec, SortSpecification<News> sortSpec);

    List<News> findAll(SearchSpecification<News> searchSpec, SortSpecification<News> sortSpec, Integer limit, Integer offset);

    List<News> findAll(SearchSpecification<News> searchSpec, Integer limit, Integer offset);

    List<News> findAll(SearchSpecification<News> searchSpec);

    List<News> findAll(Integer limit, Integer offset);

    List<News> findAll();

    long count(SearchSpecification<News> searchSpec);

    long count();
}