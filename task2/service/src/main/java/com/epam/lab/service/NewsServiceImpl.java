package com.epam.lab.service;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.repository.NewsRepo;
import com.epam.lab.repository.QuerySpecification;

import java.util.List;

public class NewsServiceImpl implements NewsService {
    private NewsRepo newsRepo;

    public NewsServiceImpl(NewsRepo newsRepo) {
        this.newsRepo = newsRepo;
    }

    @Override
    public List<NewsDto> read(QuerySpecification spec) {
        return null;
    }

    @Override
    public long create(NewsDto entity) {
        return 0;
    }

    @Override
    public boolean update(NewsDto entity) {
        return false;
    }

    @Override
    public boolean remove(NewsDto entity) {
        return false;
    }
}
