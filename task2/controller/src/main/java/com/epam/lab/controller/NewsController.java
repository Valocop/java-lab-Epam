package com.epam.lab.controller;

import com.epam.lab.criteria.Criteria;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.service.NewsService;
import com.epam.lab.validation.CreateValidation;
import com.epam.lab.validation.SearchValidator;
import com.epam.lab.validation.SortValidator;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.*;

@RestController
@RequestMapping("/news")
@Validated
public class NewsController {
    private NewsService newsService;
    private SearchValidator searchValidator;
    private SortValidator sortValidator;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public NewsDto getNews(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) long id) {
        Optional<NewsDto> newsOptional = newsService.findById(id);
        if (newsOptional.isPresent()) {
            return newsOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to find news by id");
        }
    }

    @GetMapping("/search/{search}/sort/{sort}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<NewsDto> searchAndSortNews(@PathVariable("search") String search, @PathVariable("sort") String sort) {
        Map<String, List<String>> searchMap = convertToMap(Arrays.asList(search.split(":")));
        Map<String, List<String>> sortMap = convertToMap(Arrays.asList(sort.split(":")));

        if (searchValidator.validate(searchMap).isValid() && sortValidator.validate(sortMap).isValid()) {
            List<Criteria> criteriaList = new ArrayList<>();
            addCriteriaToList(searchMap, false, criteriaList);
            addCriteriaToList(sortMap, true, criteriaList);
            return newsService.findByCriteria(criteriaList);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect search and sort params");
        }
    }

    private void addCriteriaToList(Map<String, List<String>> searchMap, boolean isSort, List<Criteria> criteriaList) {
        searchMap.forEach((s, list) -> {
            criteriaList.add(new Criteria(s, isSort, list));
        });
    }

    @GetMapping("/search/{search}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<NewsDto> searchNews(@PathVariable("search") String search) {
        List<String> searchCriteriaList = Arrays.asList(search.split(":"));
        Map<String, List<String>> searchMap = convertToMap(searchCriteriaList);
        if (searchValidator.validate(searchMap).isValid()) {
            List<Criteria> criteriaList = new ArrayList<>();
            addCriteriaToList(searchMap, false, criteriaList);
            return newsService.findByCriteria(criteriaList);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect search and sort params");
        }
    }

    @GetMapping("/sort/{sort}")
    @ResponseStatus(HttpStatus.FOUND)
    public List<NewsDto> sortNews(@PathVariable("sort") String sort) {
        List<String> sortCriteriaList = Arrays.asList(sort.split(":"));
        Map<String, List<String>> sortMap = convertToMap(sortCriteriaList);
        if (sortValidator.validate(sortMap).isValid()) {
            List<Criteria> criteriaList = new ArrayList<>();
            addCriteriaToList(sortMap, true, criteriaList);
            return newsService.findByCriteria(criteriaList);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect search and sort params");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long create(@Validated(CreateValidation.class) @RequestBody NewsDto newsDto) {
        return newsService.create(newsDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public NewsDto update(@Validated(CreateValidation.class) @RequestBody NewsDto newsDto) {
        Optional<NewsDto> newsDtoOptional = null;
        try {
            newsDtoOptional = newsService.update(newsDto);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to update news", e);
        }
        if (newsDtoOptional.isPresent()) {
            return newsDtoOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to update news");
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody Map<String, Object> body) {
        NewsDto newsDto = new NewsDto();
        try {
            newsDto.setId(Long.parseLong((String) body.get("id")));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to delete news. Check id parameter");
        }
        if (!newsService.remove(newsDto)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to delete news");
        }
    }

    private Map<String, List<String>> convertToMap(List<String> params) {
        Map<String, List<String>> result = new HashMap<>();
        try {
            params.forEach(s -> {
                List<String> strings = Arrays.asList(s.split("="));
                String key = strings.get(0);
                List<String> value = Arrays.asList(String.join("", strings.get(1)).split(","));
                result.put(key, value);
            });
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect criteria");
        }
        return result;
    }

    public SearchValidator getSearchValidator() {
        return searchValidator;
    }

    public void setSearchValidator(SearchValidator searchValidator) {
        this.searchValidator = searchValidator;
    }

    public SortValidator getSortValidator() {
        return sortValidator;
    }

    public void setSortValidator(SortValidator sortValidator) {
        this.sortValidator = sortValidator;
    }
}
