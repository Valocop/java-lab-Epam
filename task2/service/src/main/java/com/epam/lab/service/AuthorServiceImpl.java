package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.model.Author;
import com.epam.lab.repository.AuthorRepo;
import com.epam.lab.specification.QueryAuthorByIdAndNameSpec;
import com.epam.lab.specification.QueryAuthorByIdSpec;
import com.epam.lab.specification.QueryAuthorsByNewsIdSpec;
import com.epam.lab.specification.QuerySpecification;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthorServiceImpl implements AuthorService {
    private AuthorRepo authorRepo;
    private ModelMapper modelMapper;

    public AuthorServiceImpl(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public long create(AuthorDto dto) {
        return authorRepo.save(convertToEntity(dto));
    }

    @Override
    public Optional<AuthorDto> update(AuthorDto dto) {
        if (authorRepo.update(convertToEntity(dto))) {
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean remove(AuthorDto dto) {
        return authorRepo.delete(convertToEntity(dto));
    }

    @Override
    public AuthorDto findById(long id) {
        QuerySpecification findByIdSpec = new QueryAuthorByIdSpec(id);
        List<AuthorDto> authorDtoList = authorRepo.find(findByIdSpec)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        if (authorDtoList.isEmpty()) {
            throw new ServiceException("Fail to find author by id. Not founded");
        }
        return authorDtoList.get(0);
    }

    @Override
    public Optional<AuthorDto> findByIdAndName(long id, String name) {
        List<AuthorDto> authorDtoList = authorRepo.find(new QueryAuthorByIdAndNameSpec(id, name))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        if (authorDtoList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(authorDtoList.get(0));
        }
    }

    @Override
    public List<AuthorDto> findByNewsId(long newsId) {
        return authorRepo.find(new QueryAuthorsByNewsIdSpec(newsId)).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AuthorDto convertToDto(Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }

    private Author convertToEntity(AuthorDto authorDto) {
        return modelMapper.map(authorDto, Author.class);
    }
}
