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
        if (authorRepo.update(convertToEntity(dto))) {
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public boolean remove(AuthorDto dto) {
        List<NewsDto> newsDtoList = newsRepo.find(new QueryNewsByAuthorIdSpec(dto.getId())).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        newsDtoList.forEach(newsDto -> {
            boolean isNewsRemoved = newsRepo.delete(convertToEntity(newsDto));
            if (!isNewsRemoved) {
                throw new ServiceException("Failed to remove news " + newsDto.getId());
            }
        });
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

    private NewsDto convertToDto(News news) {
        return modelMapper.map(news, NewsDto.class);
    }

    private News convertToEntity(NewsDto newsDto) {
        return modelMapper.map(newsDto, News.class);
    }
}
