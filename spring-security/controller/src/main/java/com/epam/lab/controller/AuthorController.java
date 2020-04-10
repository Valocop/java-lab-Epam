package com.epam.lab.controller;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.service.AuthorService;
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
@RequestMapping("/api/authors")
@Validated
@CrossOrigin(origins = {"http://localhost:3000"})
public class AuthorController {
    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthorDto> create(@RequestBody @Validated(CreateValidation.class) AuthorDto authorDto,
                                            UriComponentsBuilder ucb) {
        AuthorDto author = authorService.create(authorDto);
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/authors/")
                .path(String.valueOf(author.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(author, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.FOUND)
    public AuthorDto read(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) long id) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(id);
        Optional<AuthorDto> optionalAuthorDto = authorService.read(authorDto);
        return optionalAuthorDto.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<AuthorDto> readAll() {
        return authorService.readAll();
    }

    @PutMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDto update(@RequestBody @Validated(UpdateValidation.class) AuthorDto authorDto) {
        Optional<AuthorDto> dtoOptional = authorService.update(authorDto);
        return dtoOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) long id) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(id);
        authorService.delete(authorDto);
    }
}
