package com.epam.lab.service;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.repository.NewsRepo;

public class NewsServiceImpl implements NewsService {
    private NewsRepo newsRepo;

    public NewsServiceImpl(NewsRepo newsRepo) {
        this.newsRepo = newsRepo;
    }

    @Override
    public long create(NewsDto dto) {
        return 0;
    }

    @Override
    public boolean update(NewsDto dto) {
        return false;
    }

    @Override
    public boolean remove(NewsDto dto) {
        return false;
    }
}
