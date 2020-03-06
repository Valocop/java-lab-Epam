package com.epam.lab.controller;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.service.NewsService;
import com.epam.lab.validation.CreateValidation;
import com.epam.lab.validation.UpdateValidation;
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
import java.util.List;
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
    public ResponseEntity<NewsDto> create(@Validated(CreateValidation.class) @RequestBody NewsDto newsDto,
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
    @ResponseStatus(HttpStatus.FOUND)
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
    @ResponseStatus(HttpStatus.FOUND)
    public List<NewsDto> readBySpecification(@RequestParam(name = "authors_name", required = false) List<String> authorsName,
                                             @RequestParam(name = "tags_name", required = false) List<String> tagsName,
                                             @RequestParam(name = "sort", required = false) List<String> sorts) {
        if (isSearchCorrect(authorsName, tagsName)) {
            return newsService.findBySpecification(authorsName, tagsName, sorts);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check search parameters");
    }

    @PutMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public NewsDto update(@Validated(UpdateValidation.class) @RequestBody NewsDto newsDto) {
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

    private boolean isSearchCorrect(List<String> authorsName, List<String> tagsName) {
        return !(authorsName == null && tagsName == null);
    }
}
