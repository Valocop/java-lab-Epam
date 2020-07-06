package com.epam.lab.service;

import com.epam.lab.criteria.Criteria;
import com.epam.lab.dto.NewsDto;

import java.util.List;
import java.util.Optional;

public interface NewsService extends Service<NewsDto> {
    Optional<NewsDto> findById(long id);
    List<NewsDto> findByCriteria(List<Criteria> criteriaList);
    List<NewsDto> findByAuthorId(long authorId);
    List<NewsDto> findByTagId(long tagId);
    boolean deleteTagOfNews(long newsId, long tagId);
}
