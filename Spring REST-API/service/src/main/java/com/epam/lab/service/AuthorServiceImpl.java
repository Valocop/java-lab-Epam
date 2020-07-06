package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.repository.AuthorRepo;
import com.epam.lab.repository.NewsRepo;
import com.epam.lab.specification.*;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthorServiceImpl implements AuthorService {
    private AuthorRepo authorRepo;
    private NewsRepo newsRepo;
    private ModelMapper modelMapper;

    public AuthorServiceImpl(AuthorRepo authorRepo, NewsRepo newsRepo) {
        this.authorRepo = authorRepo;
        this.newsRepo = newsRepo;
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
        return authorRepo.update(convertToEntity(dto)) ? Optional.of(dto) : Optional.empty();
    }

    @Transactional
    @Override
    public boolean remove(AuthorDto dto) {
        List<NewsDto> newsDtoList = findNewsDtoByAuthorDto(dto);
        newsDtoList.forEach(newsDto -> {
            if (isNewsNotRemoved(newsRepo.delete(convertToEntity(newsDto)))) {
                throw new ServiceException("Failed to remove news " + newsDto.getId());
            }
        });
        return authorRepo.delete(convertToEntity(dto));
    }

    private boolean isNewsNotRemoved(boolean isNewsRemoved) {
        return !isNewsRemoved;
    }

    private List<NewsDto> findNewsDtoByAuthorDto(AuthorDto dto) {
        return newsRepo.findBy(new FindNewsByAuthorIdSpec(dto.getId())).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AuthorDto> findById(long id) {
        FindSpecification findByIdSpec = new FindAuthorByIdSpec(id);
        List<AuthorDto> authorDtoList = authorRepo.findBy(findByIdSpec)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return authorDtoList.isEmpty() ? Optional.empty() : Optional.of(authorDtoList.get(0));
    }

    @Override
    public Optional<AuthorDto> findByIdAndName(long id, String name) {
        List<AuthorDto> authorDtoList = authorRepo.findBy(new FindAuthorByIdAndNameSpec(id, name))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return authorDtoList.isEmpty() ? Optional.empty() : Optional.of(authorDtoList.get(0));
    }

    @Override
    public List<AuthorDto> findByNewsId(long newsId) {
        return authorRepo.findBy(new FindAuthorsByNewsIdSpec(newsId)).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private AuthorDto convertToDto(Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }

    private Author convertToEntity(AuthorDto authorDto) {
        return modelMapper.map(authorDto, Author.class);
    }

    private NewsDto convertToDto(News news) {
        return modelMapper.map(news, NewsDto.class);
    }

    private News convertToEntity(NewsDto newsDto) {
        return modelMapper.map(newsDto, News.class);
    }
}
