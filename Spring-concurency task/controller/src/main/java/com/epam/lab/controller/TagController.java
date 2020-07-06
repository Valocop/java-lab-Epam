package com.epam.lab.controller;

import com.epam.lab.dto.TagDto;
import com.epam.lab.service.TagService;
import com.epam.lab.validation.CreatingValidation;
import com.epam.lab.validation.UpdatingValidation;
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
@RequestMapping("/api/tags")
@Validated
@CrossOrigin(origins = {"http://localhost:3000"})
public class TagController {
    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TagDto> create(@Validated(CreatingValidation.class) @RequestBody TagDto tagDto,
                                         UriComponentsBuilder ucb) {
        TagDto tag = tagService.create(tagDto);
        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/tags/")
                .path(String.valueOf(tag.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(tag, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.FOUND)
    public TagDto read(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) long id) {
        TagDto tagDto = new TagDto();
        tagDto.setId(id);
        Optional<TagDto> optionalTagDto = tagService.read(tagDto);
        return optionalTagDto.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> readAll() {
        return tagService.readAll();
    }

    @PutMapping(produces = "application/json",
            consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public TagDto update(@Validated(UpdatingValidation.class) @RequestBody TagDto tagDto) {
        Optional<TagDto> tagDtoOptional = tagService.update(tagDto);
        return tagDtoOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") @Min(1) @Max(Long.MAX_VALUE) long id) {
        TagDto tagDto = new TagDto();
        tagDto.setId(id);
        tagService.delete(tagDto);
    }
}
