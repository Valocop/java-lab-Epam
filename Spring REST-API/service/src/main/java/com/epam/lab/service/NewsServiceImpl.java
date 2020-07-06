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
import com.epam.lab.specification.*;
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
        saveAuthorIfAbsent(authorDto);
        List<TagDto> actualTags = createAbsentTags(dto);
        long newsId = newsRepo.save(convertToEntity(dto));
        dto.setId(newsId);
        newsRepo.createBindingOfAuthorAndNews(convertToEntity(dto), convertToEntity(authorDto));
        actualTags.forEach(tagDto -> newsRepo.createBindingOfNewsAndTags(convertToEntity(dto), convertToEntity(tagDto)));
        return dto.getId();
    }

    @Transactional
    @Override
    public Optional<NewsDto> update(NewsDto dto) {
        AuthorDto authorDto = dto.getAuthorDto();
        saveNewsIfAbsent(dto);
        saveAuthorIfAbsent(authorDto);
        updateNewsAndTags(dto, authorDto);
        updateTagsOfNews(dto);
        return Optional.of(dto);
    }

    @Transactional
    @Override
    public boolean remove(NewsDto dto) {
        newsRepo.deleteBindingsOfNewsAndAutors(convertToEntity(dto));
        newsRepo.deleteBindingsOfNewsAndAllTags(convertToEntity(dto));
        tagService.deleteUnsignedTags();
        return newsRepo.delete(convertToEntity(dto));
    }

    @Transactional
    @Override
    public Optional<NewsDto> findById(long id) {
        List<News> newsList = newsRepo.findBy(new FindNewsByIdSpec(id));

        if (isNewsListNotEmpty(newsList.isEmpty())) {
            NewsDto newsDto = newsList.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList()).get(0);
            List<TagDto> tagDtoList = tagService.findByNewsId(id);
            List<AuthorDto> authorDtoList = getAuthorsByNewsId(id);
            if (isAuthorNotPresent(authorDtoList.isEmpty())) {
                newsDto.setAuthorDto(authorDtoList.get(0));
                newsDto.setTagDtoList(tagDtoList);
                return Optional.of(newsDto);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<NewsDto> findByCriteria(List<Criteria> criteriaList) {
        FindSpecification findByCriteriaSpec = buildSpecByListOfCriteria(criteriaList);
        Set<NewsDto> newsDtoSet = findNewsBySpecification(findByCriteriaSpec);
        return new ArrayList<>(newsDtoSet);
    }

    @Override
    public List<NewsDto> findByAuthorId(long authorId) {
        return newsRepo.findBy(new FindNewsByAuthorIdSpec(authorId)).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NewsDto> findByTagId(long tagId) {
        return newsRepo.findBy(new FindNewsByTagIdSpec(tagId)).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteTagOfNews(long newsId, long tagId) {
        News news = new News();
        news.setId(newsId);
        Tag tag = new Tag();
        tag.setId(tagId);
        return newsRepo.deleteBindingOfNewsAndTag(news, tag);
    }

    private Set<NewsDto> findNewsBySpecification(FindSpecification findByCriteriaSpec) {
        Set<NewsDto> newsDtoSet = newsRepo.findBy(findByCriteriaSpec).stream()
                .map(this::convertToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        newsDtoSet.forEach(newsDto -> {
            List<AuthorDto> authorDtoList = getAuthorsByNewsId(newsDto.getId());
            if (isAuthorNotPresent(authorDtoList.isEmpty())) {
                newsDto.setAuthorDto(authorDtoList.get(0));
            }
            List<TagDto> tagDtoList = tagService.findByNewsId(newsDto.getId());
            newsDto.setTagDtoList(tagDtoList);
        });
        return newsDtoSet;
    }

    private FindSpecification buildSpecByListOfCriteria(List<Criteria> criteriaList) {
        FindSpecification findByCriteriaSpec = new FindNewsSpec();
        for (Criteria criteria : criteriaList) {
            findByCriteriaSpec = findByCriteriaSpec.add(criteria);
        }
        return findByCriteriaSpec;
    }

    private boolean isNewsListNotEmpty(boolean empty) {
        return !empty;
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

    private boolean isAuthorNotPresent(boolean present) {
        return !present;
    }

    private List<TagDto> createAbsentTags(NewsDto dto) {
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
        return actualTags;
    }

    private void updateNewsAndTags(NewsDto dto, AuthorDto authorDto) {
        List<AuthorDto> authorOfNewsList = getAuthorsByNewsId(dto.getId());
        if (isAuthorNotPresent(authorOfNewsList.isEmpty())) {
            final AuthorDto authorOfNews = authorOfNewsList.get(0);
            if (authorOfNews.getId() != authorDto.getId()) {
                updateNewsAndAuthorBinding(dto, authorDto);
            }
            if (isNotUpdatedNewsOfAuthor(dto)) {
                throw new ServiceException("Failed to update news");
            }
        } else {
            if (isNotUpdated(createNewsToAuthor(authorDto, dto), updateNewsOfAuthor(dto))) {
                throw new ServiceException("Failed to create author to news and update news");
            }
        }
    }

    private void saveAuthorIfAbsent(AuthorDto authorDto) {
        Optional<AuthorDto> authorOptional = authorService.findByIdAndName(authorDto.getId(), authorDto.getName());
        if (isAuthorNotPresent(authorOptional.isPresent())) {
            saveAuthor(authorDto);
        }
    }

    private void saveNewsIfAbsent(NewsDto dto) {
        List<NewsDto> newsDtoList = getNewsById(dto);
        if (newsDtoList.isEmpty()) {
            long newsId = newsRepo.save(convertToEntity(dto));
            dto.setId(newsId);
        }
    }

    private void updateNewsAndAuthorBinding(NewsDto dto, AuthorDto authorDto) {
        boolean isAuthorOfNewsDeleted = newsRepo.deleteBindingsOfNewsAndAutors(convertToEntity(dto));
        boolean isAuthorToNewsCreated = newsRepo.createBindingOfAuthorAndNews(convertToEntity(dto),
                convertToEntity(authorDto));
        if (isNotUpdated(isAuthorOfNewsDeleted, isAuthorToNewsCreated)) {
            throw new ServiceException("Failed to create author to news");
        }
    }

    private boolean isNotUpdated(boolean isOne, boolean isTwo) {
        return !(isOne && isTwo);
    }

    private List<AuthorDto> getAuthorsByNewsId(long id) {
        return authorService.findByNewsId(id);
    }

    private void saveAuthor(AuthorDto authorDto) {
        authorDto.setId(authorService.create(authorDto));
    }

    private List<NewsDto> getNewsById(NewsDto dto) {
        return newsRepo.findBy(new FindNewsByIdSpec(dto.getId()))
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private void updateTagsOfNews(NewsDto newsDto) {
        List<TagDto> tagDtoList = createAbsentTags(newsDto);
        newsRepo.deleteBindingsOfNewsAndAllTags(convertToEntity(newsDto));
        tagDtoList.forEach(tagDto -> newsRepo.createBindingOfNewsAndTags(convertToEntity(newsDto), convertToEntity(tagDto)));
        tagService.deleteUnsignedTags();
    }

    private boolean createNewsToAuthor(AuthorDto authorDto, NewsDto newsDto) {
        return newsRepo.createBindingOfAuthorAndNews(convertToEntity(newsDto), convertToEntity(authorDto));
    }

    private boolean updateNewsOfAuthor(NewsDto newsDto) {
        return newsRepo.update(convertToEntity(newsDto));
    }

    private boolean isNotUpdatedNewsOfAuthor(NewsDto newsDto) {
        return !newsRepo.update(convertToEntity(newsDto));
    }
}
