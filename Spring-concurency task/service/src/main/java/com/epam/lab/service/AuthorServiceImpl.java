package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.model.Author;
import com.epam.lab.repository.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.lab.service.ServiceUtil.convertToDto;
import static com.epam.lab.service.ServiceUtil.convertToEntity;

@Service
public class AuthorServiceImpl implements AuthorService {
    private AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorDto create(AuthorDto dto) {
        Author author = authorRepository.save(convertToEntity(dto));
        return convertToDto(author);
    }

    @Override
    public Optional<AuthorDto> read(AuthorDto dto) {
        Optional<Author> optionalAuthor = authorRepository.findById(dto.getId());
        return optionalAuthor.map(ServiceUtil::convertToDto);
    }

    @Override
    public Optional<AuthorDto> update(AuthorDto dto) {
        Optional<Author> optionalAuthor = authorRepository.findById(dto.getId());
        if (optionalAuthor.isPresent()) {
            Author author = authorRepository.update(convertToEntity(dto));
            return Optional.of(convertToDto(author));
        }
        return Optional.empty();
    }

    @Override
    public void delete(AuthorDto dto) {
        authorRepository.delete(convertToEntity(dto));
    }

    @Override
    public List<AuthorDto> readAll() {
        return authorRepository.readAll().stream()
                .map(ServiceUtil::convertToDto)
                .collect(Collectors.toList());
    }
}
