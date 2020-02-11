package com.epam.lab.controller;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.service.AuthorService;
import com.epam.lab.validation.CreateValidation;
import com.epam.lab.validation.FullValidation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/authors")
@Validated
public class AuthorController {
    private AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public AuthorDto findById(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) long id) {
        try {
            return authorService.findById(id);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long create(@Validated(CreateValidation.class) @RequestBody AuthorDto authorDto) {
        return authorService.create(authorDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public AuthorDto update(@Validated(FullValidation.class) @RequestBody AuthorDto authorDto) {
        if (authorService.update(authorDto).isPresent()) {
            return authorDto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to update author");
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@Validated(FullValidation.class) @RequestBody AuthorDto authorDto) {
        if (!authorService.remove(authorDto)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to delete author");
        }
    }
}
