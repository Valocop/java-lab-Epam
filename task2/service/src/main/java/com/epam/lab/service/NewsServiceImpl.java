package com.epam.lab.service;

import com.epam.lab.model.News;
import com.epam.lab.repository.NewsRepo;
import com.epam.lab.repository.QuerySpecification;

import java.util.List;

public class NewsServiceImpl implements NewsService {
    private NewsRepo newsRepo;

    public NewsServiceImpl(NewsRepo newsRepo) {
        this.newsRepo = newsRepo;
    }

    @Override
    public List<News> read(QuerySpecification spec) {
        return newsRepo.find(spec);
    }

    @Override
    public long create(News entity) {
        return newsRepo.save(entity);
    }

    @Override
    public boolean update(News entity) {
        return newsRepo.update(entity);
    }

    @Override
    public boolean remove(News entity) {
        return newsRepo.delete(entity);
    }
}
