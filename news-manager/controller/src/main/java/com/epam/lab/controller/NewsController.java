package com.epam.lab.controller;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.service.NewsService;
import com.epam.lab.validation.CreatingValidation;
import com.epam.lab.validation.UpdatingValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/news")
@Validated
@CrossOrigin(origins = {"http://localhost:3000"})
public class NewsController {
    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NewsDto> create(@Validated(CreatingValidation.class) @RequestBody NewsDto newsDto,
                                          UriComponentsBuilder ucb) {
        NewsDto news = newsService.create(newsDto);
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/news/")
                .path(String.valueOf(news.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(news, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public NewsDto read(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) long id) {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(id);
        Optional<NewsDto> optionalNewsDto = newsService.read(newsDto);
        return optionalNewsDto.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "News not found"));
    }

    @GetMapping(path = "/count", produces = "application/json")
    @ResponseStatus(HttpStatus.FOUND)
    public long readCountOfNews() {
        return newsService.getCountOfNews();
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, Object>> getNews(@RequestParam(name = "authors_name", required = false) List<String> authorsName,
                                                       @RequestParam(name = "tags_name", required = false) List<String> tagsName,
                                                       @RequestParam(name = "sort", required = false, defaultValue = "creation_date") List<String> sorts,
                                                       @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) Integer count,
                                                       @RequestParam(required = false, defaultValue = "1") @Min(1) Integer page) {
        List<NewsDto> news = newsService.findNews(authorsName, tagsName, sorts, count, page);
        long countOfNews = newsService.getCountOfNews(authorsName, tagsName);

        Map<String, Object> body = new HashMap<>();
        body.put("news", news);
        body.put("count", countOfNews);
        return ResponseEntity.status(HttpStatus.OK)
                .body(body);
    }

    @PutMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public NewsDto update(@Validated(UpdatingValidation.class) @RequestBody NewsDto newsDto) {
        Optional<NewsDto> dtoOptional = newsService.update(newsDto);
        return dtoOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "News not found"));
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) long id) {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(id);
        newsService.delete(newsDto);
    }
}
