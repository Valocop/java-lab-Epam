package com.epam.lab.controller;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.service.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    private NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/search{params}")
    public List<NewsDto> searchNews(@PathVariable("params") String params) {
        List<String> criteriaStrings = Arrays.asList(params.split(","));

        return null;
    }

    @GetMapping("/{id}")
    public List<NewsDto> selectNews(@PathVariable("id") long id) {
        return null;
    }
}
