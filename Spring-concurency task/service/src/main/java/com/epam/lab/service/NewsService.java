package com.epam.lab.service;

import com.epam.lab.dto.NewsDto;

import java.util.List;

public interface NewsService extends Service<NewsDto> {

    List<NewsDto> findNews(List<String> authorsName, List<String> tagsName, List<String> sorts,
                           Integer count, Integer page);

    long getCountOfNews();

    long getCountOfNews(List<String> authorsName, List<String> tagsName);
}
