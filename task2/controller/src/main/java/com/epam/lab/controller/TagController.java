package com.epam.lab.controller;

import com.epam.lab.dto.TagDto;
import com.epam.lab.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tags")
@Validated
public class TagController {
    private TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/{id}")
    public TagDto findById(@PathVariable("id") long id) {
        return tagService.findById(id);
    }

//    @PostMapping(value = "/")
//    @ResponseStatus(HttpStatus.CREATED)
//    public long create(@Valid @RequestBody TagDto tagDto) {
//        return tagService.create(tagDto);
//    }

    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public long create(@Valid @RequestBody TagDto tagDto) {
        return tagService.create(tagDto);
    }
}
