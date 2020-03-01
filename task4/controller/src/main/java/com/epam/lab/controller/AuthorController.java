package com.epam.lab.controller;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/authors")
@Validated
public class AuthorController {
    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthorDto> create(@RequestBody @Valid AuthorDto authorDto, UriComponentsBuilder ucb) {
        AuthorDto author = authorService.create(authorDto);
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/authors/")
                .path(String.valueOf(author.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<AuthorDto>(author, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.FOUND)
    public AuthorDto read(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) long id) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(id);
        Optional<AuthorDto> optionalAuthorDto = authorService.read(authorDto);
        return optionalAuthorDto.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    }

    @PutMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDto update(@RequestBody @Valid AuthorDto authorDto) {
        return authorService.update(authorDto);
    }

    @DeleteMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestBody @Valid AuthorDto authorDto) {
        authorService.delete(authorDto);
    }
}
