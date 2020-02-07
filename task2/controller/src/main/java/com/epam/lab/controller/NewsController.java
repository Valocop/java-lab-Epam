package com.epam.lab.controller;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.service.NewsService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/news")
public class NewsController {
    private NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

//    @GetMapping("/")
//    public List<NewsDto> searchNews(@RequestParam Map<String, String> params) {
//        AuthorDto authorDto = new AuthorDto();
//        authorDto.setName("Test");
//        authorDto.setSurname("Test Test");
//        TagDto tagDto = new TagDto();
//        tagDto.setName("Tag");
//        NewsDto newsDto = new NewsDto();
//        newsDto.setAuthorDto(authorDto);
//        newsDto.setDtoList(Collections.singletonList(tagDto));
//        return Collections.singletonList(newsDto);
//    }

//    @GetMapping("/{id}")
//    public List<NewsDto> selectNews(@PathVariable("id") long id) {
//        AuthorDto authorDto = new AuthorDto();
//        authorDto.setName("Test");
//        authorDto.setSurname("Test Test");
//        TagDto tagDto = new TagDto();
//        tagDto.setName("Tag");
//        NewsDto newsDto = new NewsDto();
//        newsDto.setAuthorDto(authorDto);
//        newsDto.setDtoList(Collections.singletonList(tagDto));
//        return Collections.singletonList(newsDto);
//    }

    @GetMapping("/")
    public String selectNews() {
        return "String news newwwww";
    }
}
