package com.epam.lab.service;

import com.epam.lab.dto.NewsDto;

import java.util.List;

public interface NewsService extends Service<NewsDto> {

    List<NewsDto> findBySpecification(List<String> authorsName, List<String> tagsName, List<String> sorts);

    List<NewsDto> findBySpecification(List<String> authorsName, List<String> tagsName, List<String> sorts,
                                      Integer limit, Integer offset);

    List<NewsDto> findAll();

    List<NewsDto> findAll(Integer limit, Integer offset);

    long getCountOfNews(List<String> authorsName, List<String> tagsName);

    long getCountOfNews();
}
