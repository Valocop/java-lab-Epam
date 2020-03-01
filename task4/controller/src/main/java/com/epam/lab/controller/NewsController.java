package com.epam.lab.controller;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.service.NewsService;
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
import java.util.Optional;

@RestController
@RequestMapping("/news")
@Validated
public class NewsController {
    private NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NewsDto> create(@Validated @RequestBody NewsDto newsDto, UriComponentsBuilder ucb) {
        NewsDto news = newsService.create(newsDto);
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/news/")
                .path(String.valueOf(news.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<NewsDto>(news, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.FOUND)
    public NewsDto read(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) long id) {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(id);
        Optional<NewsDto> optionalNewsDto = newsService.read(newsDto);
        return optionalNewsDto.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "News not found"));
    }

    @PutMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public NewsDto update(@Validated @RequestBody NewsDto newsDto) {
        return newsService.update(newsDto);
    }

    @DeleteMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Validated @RequestBody NewsDto newsDto) {
        newsService.delete(newsDto);
    }
}
