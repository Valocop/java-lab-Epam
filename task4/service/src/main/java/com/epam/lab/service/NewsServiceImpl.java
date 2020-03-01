package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.model.News;
import com.epam.lab.repository.NewsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {
    private NewsRepository newsRepository;
    private AuthorService authorService;
    private TagService tagService;
    private ModelMapper modelMapper;

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository, AuthorService authorService,
                           TagService tagService, ModelMapper modelMapper) {
        this.newsRepository = newsRepository;
        this.authorService = authorService;
        this.tagService = tagService;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public NewsDto create(NewsDto dto) {
        AuthorDto authorDto = dto.getAuthorDto();
        List<TagDto> tagDtoList = dto.getTagDtoList();
        createNotExistAuthor(authorDto);
        createNotExistTags(tagDtoList);
        News news = newsRepository.save(convertToEntity(dto));
        return convertToDto(news);
    }

    private void createNotExistTags(List<TagDto> tagDtoList) {
        tagDtoList.forEach(tagDto -> {
            if (isTagNotExist(tagDto)) {
                tagService.create(tagDto);
            }
        });
    }

    private void createNotExistAuthor(AuthorDto authorDto) {
        if (isAuthorNotExist(authorDto)) {
            authorService.create(authorDto);
        }
    }

    @Override
    public Optional<NewsDto> read(NewsDto dto) {
        Optional<News> newsOptional = newsRepository.findById(dto.getId());
        return newsOptional.map(this::convertToDto);
    }

    @Override
    @Transactional
    public NewsDto update(NewsDto dto) {
        createNotExistAuthor(dto.getAuthorDto());
        createNotExistTags(dto.getTagDtoList());
        News news = newsRepository.update(convertToEntity(dto));
        return convertToDto(news);
    }

    @Override
    public void delete(NewsDto dto) {
        newsRepository.delete(convertToEntity(dto));
    }

    private NewsDto convertToDto(News news) {
        return modelMapper.map(news, NewsDto.class);
    }

    private News convertToEntity(NewsDto newsDto) {
        return modelMapper.map(newsDto, News.class);
    }

    private boolean isAuthorNotExist(AuthorDto authorDto) {
        return !authorService.read(authorDto).isPresent();
    }

    private boolean isTagNotExist(TagDto tagDto) {
        return !tagService.read(tagDto).isPresent();
    }
}
