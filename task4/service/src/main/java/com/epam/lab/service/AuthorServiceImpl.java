package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.model.Author;
import com.epam.lab.repository.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private AuthorRepository authorRepository;
    private ModelMapper modelMapper;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AuthorDto create(AuthorDto dto) {
        Author author = authorRepository.save(convertToEntity(dto));
        return convertToDto(author);
    }

    @Override
    public Optional<AuthorDto> read(AuthorDto dto) {
        Optional<Author> optionalAuthor = authorRepository.findById(dto.getId());
        return optionalAuthor.map(this::convertToDto);
    }

    @Override
    public AuthorDto update(AuthorDto dto) {
        Author author = authorRepository.update(convertToEntity(dto));
        return convertToDto(author);
    }

    @Override
    public void delete(AuthorDto dto) {
        authorRepository.delete(convertToEntity(dto));
    }

    private AuthorDto convertToDto(Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }

    private Author convertToEntity(AuthorDto authorDto) {
        return modelMapper.map(authorDto, Author.class);
    }
}
