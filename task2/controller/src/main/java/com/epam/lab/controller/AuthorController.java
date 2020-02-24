package com.epam.lab.controller;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.service.AuthorService;
import com.epam.lab.validation.CreateValidation;
import com.epam.lab.validation.FullValidation;
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

@RestController
@RequestMapping("/authors")
@Validated
public class AuthorController {
    private AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.FOUND)
    public AuthorDto findById(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) long id) {
        try {
            if (authorService.findById(id).isPresent()) {
                return authorService.findById(id).get();
            }
            throw new ServiceException("Failed to find author");
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthorDto> create(@Validated(CreateValidation.class) @RequestBody AuthorDto authorDto,
                                            UriComponentsBuilder ucb) {
        long id = authorService.create(authorDto);
        authorDto.setId(id);

        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/authors/")
                .path(String.valueOf(authorDto.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);

        return new ResponseEntity<AuthorDto>(authorDto, headers, HttpStatus.CREATED);
    }

    @PutMapping(produces = "application/json")
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
        if (authorIsNotDeleted(authorDto)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to delete author");
        }
    }

    private boolean authorIsNotDeleted(@RequestBody @Validated(FullValidation.class) AuthorDto authorDto) {
        return !authorService.remove(authorDto);
    }


}
