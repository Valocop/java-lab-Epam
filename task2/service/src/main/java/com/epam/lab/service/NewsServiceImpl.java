package com.epam.lab.service;

import com.epam.lab.criteria.Criteria;
import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.NewsRepo;
import com.epam.lab.specification.QueryNewsByIdSpec;
import com.epam.lab.specification.QueryNewsSpec;
import com.epam.lab.specification.QuerySpecification;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

public class NewsServiceImpl implements NewsService {
    private NewsRepo newsRepo;
    private ModelMapper modelMapper;
    private AuthorService authorService;
    private TagService tagService;

    public NewsServiceImpl(NewsRepo newsRepo, AuthorService authorService, TagService tagService) {
        this.newsRepo = newsRepo;
        this.authorService = authorService;
        this.tagService = tagService;
    }

    @Transactional
    @Override
    public long create(NewsDto dto) {
        AuthorDto authorDto = dto.getAuthorDto();
        Optional<AuthorDto> authorOptional = authorService.findByIdAndName(authorDto.getId(), authorDto.getName());
        if (!authorOptional.isPresent()) {
            authorDto.setId(authorService.create(authorDto));
        }
        List<TagDto> actualTags = new ArrayList<>(dto.getTagDtoList());
        actualTags.forEach(tagDto -> {
            List<TagDto> tagByName = tagService.findByName(tagDto.getName());
            if (tagByName.isEmpty()) {
                long id = tagService.create(tagDto);
                tagDto.setId(id);
            } else {
                tagDto.setId(tagByName.get(0).getId());
            }
        });
        Long newsId = newsRepo.save(convertToEntity(dto));
        dto.setId(newsId);
        boolean authorToNews = newsRepo.createAuthorToNews(convertToEntity(dto), convertToEntity(authorDto));
        actualTags.forEach(tagDto -> {
            newsRepo.createTagToNews(convertToEntity(dto), convertToEntity(tagDto));
        });
        return dto.getId();
    }

    @Transactional
    @Override
    public Optional<NewsDto> update(NewsDto dto) {
        AuthorDto authorDto = dto.getAuthorDto();
        Optional<AuthorDto> authorOptional = authorService.findByIdAndName(authorDto.getId(), authorDto.getName());
        List<NewsDto> newsDtoList = newsRepo.find(new QueryNewsByIdSpec(dto.getId()))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        if (newsDtoList.isEmpty()) {
            dto.setId(newsRepo.save(convertToEntity(dto)));
        }
        if (!authorOptional.isPresent()) {
            authorDto.setId(authorService.create(authorDto));
        }
        List<AuthorDto> authorOfNewsList = authorService.findByNewsId(dto.getId());
        if (!authorOfNewsList.isEmpty()) {
            if (!(authorOfNewsList.get(0).getId() == authorDto.getId())) {
                boolean isAuthorOfNewsDeleted = newsRepo.deleteAuthorOfNews(convertToEntity(dto));
                boolean isAuthorToNewsCreated = newsRepo.createAuthorToNews(convertToEntity(dto),
                        convertToEntity(authorDto));
                if (!(isAuthorOfNewsDeleted && isAuthorToNewsCreated)) {
                    throw new ServiceException("Failed to create author to news");
                }
            }
            if (!updateNewsOfAuthor(dto)) {
                throw new ServiceException("Failed to update news");
            }
        } else {
            boolean isNewsCreatedToAuthor = createNewsToAuthor(authorDto, dto);
            boolean isNewsUpdated = updateNewsOfAuthor(dto);
            if (!(isNewsCreatedToAuthor && isNewsUpdated)) {
                throw new ServiceException("Failed to create author to news and update news");
            }
        }
        updateTagsOfNews(dto);
        return Optional.of(dto);
    }

    private void updateTagsOfNews(NewsDto newsDto) {
        List<TagDto> tagDtoList = new ArrayList<>(newsDto.getTagDtoList());
        tagDtoList.forEach(tagDto -> {
            List<TagDto> tagByName = tagService.findByName(tagDto.getName());
            if (tagByName.isEmpty()) {
                long id = tagService.create(tagDto);
                tagDto.setId(id);
            } else {
                tagDto.setId(tagByName.get(0).getId());
            }
        });
        newsRepo.deleteTagsOfNews(convertToEntity(newsDto));
        tagDtoList.forEach(tagDto -> newsRepo.createTagToNews(convertToEntity(newsDto), convertToEntity(tagDto)));
        tagService.deleteUnsignedTags();
    }

    private boolean createNewsToAuthor(AuthorDto authorDto, NewsDto newsDto) {
        return newsRepo.createAuthorToNews(convertToEntity(newsDto), convertToEntity(authorDto));
    }

    private boolean updateNewsOfAuthor(NewsDto newsDto) {
        return newsRepo.update(convertToEntity(newsDto));
    }

    @Transactional
    @Override
    public boolean remove(NewsDto dto) {
        newsRepo.deleteAuthorOfNews(convertToEntity(dto));
        newsRepo.deleteTagsOfNews(convertToEntity(dto));
        tagService.deleteUnsignedTags();
        return newsRepo.delete(convertToEntity(dto));
    }

    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    private NewsDto convertToDto(News news) {
        return modelMapper.map(news, NewsDto.class);
    }

    private News convertToEntity(NewsDto newsDto) {
        return modelMapper.map(newsDto, News.class);
    }

    private Author convertToEntity(AuthorDto authorDto) {
        return modelMapper.map(authorDto, Author.class);
    }

    private Tag convertToEntity(TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }

    @Transactional
    @Override
    public Optional<NewsDto> findById(long id) {
        List<News> newsList = newsRepo.find(new QueryNewsByIdSpec(id));
        if (newsList.isEmpty()) {
            return Optional.empty();
        } else {
            NewsDto newsDto = newsList.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList()).get(0);
            List<TagDto> tagDtoList = tagService.findByNewsId(id);
            List<AuthorDto> authorDtoList = authorService.findByNewsId(id);
            if (!authorDtoList.isEmpty()) {
                newsDto.setAuthorDto(authorDtoList.get(0));
                newsDto.setTagDtoList(tagDtoList);
                return Optional.of(newsDto);
            } else {
                return Optional.empty();
            }
        }
    }

    @Override
    public List<NewsDto> findByCriteria(List<Criteria> criteriaList) {
        QuerySpecification findByCriteriaSpec = new QueryNewsSpec();
        for (Criteria criteria : criteriaList) {
            findByCriteriaSpec = findByCriteriaSpec.add(criteria);
        }
        Set<NewsDto> newsDtoSet = newsRepo.find(findByCriteriaSpec).stream()
                .map(this::convertToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        newsDtoSet.forEach(newsDto -> {
            List<AuthorDto> authorDtoList = authorService.findByNewsId(newsDto.getId());
            if (!authorDtoList.isEmpty()) {
                newsDto.setAuthorDto(authorDtoList.get(0));
            }
            List<TagDto> tagDtoList = tagService.findByNewsId(newsDto.getId());
            newsDto.setTagDtoList(tagDtoList);
        });
        return new ArrayList<>(newsDtoSet);
    }
}
