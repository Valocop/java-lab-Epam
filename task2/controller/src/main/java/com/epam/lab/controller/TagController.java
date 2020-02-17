package com.epam.lab.controller;

import com.epam.lab.dto.TagDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.service.TagService;
import com.epam.lab.validation.CreateValidation;
import com.epam.lab.validation.FullValidation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/tags")
@Validated
public class TagController {
    private TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public TagDto findById(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) long id) {
        try {
            return tagService.findById(id);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long create(@Validated(CreateValidation.class) @RequestBody TagDto tagDto) {
        return tagService.create(tagDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TagDto update(@Validated(FullValidation.class) @RequestBody TagDto tagDto) {
        if (tagService.update(tagDto).isPresent()) {
            return tagDto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to update tag");
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Validated(FullValidation.class) @RequestBody TagDto tagDto) {
        if (isTagNotRemoved(tagDto)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to delete tag");
        }
    }

    private boolean isTagNotRemoved(@RequestBody @Validated(FullValidation.class) TagDto tagDto) {
        return !tagService.remove(tagDto);
    }
}
