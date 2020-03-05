package com.epam.lab.service;

import com.epam.lab.dto.NewsDto;

import java.util.List;

public interface NewsService extends Service<NewsDto> {

    List<NewsDto> findBySpecification(List<String> authorsName, List<String> tagsName, List<String> sorts);

    long getCountOfNews();
}
