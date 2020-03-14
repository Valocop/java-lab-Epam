package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.model.News;
import com.epam.lab.repository.NewsRepository;
import com.epam.lab.specification.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        AuthorDto authorDto = dto.getAuthor();
        Set<TagDto> tagDtoSet = dto.getTags();
        AuthorDto author = createAuthorIfNotExist(authorDto);
        Set<TagDto> tags = createTagsIfNotExist(tagDtoSet);
        dto.setAuthor(author);
        dto.setTags(tags);
        News news = newsRepository.save(convertToEntity(dto));
        NewsDto newsDto = convertToDto(news);
        newsDto.setAuthor(author);
        newsDto.setTags(tags);
        return newsDto;
    }

    private Set<TagDto> createTagsIfNotExist(Set<TagDto> tagDtoSet) {
        Set<TagDto> tagsDto = new HashSet<>();
        for (TagDto tagDto : tagDtoSet) {
            Optional<TagDto> dtoOptional = tagService.read(tagDto);
            TagDto dto = dtoOptional.orElseGet(() -> {
                tagDto.setId(0);
                return tagService.create(tagDto);
            });
            tagsDto.add(dto);
        }
        return tagsDto;
    }

    private AuthorDto createAuthorIfNotExist(AuthorDto authorDto) {
        Optional<AuthorDto> dtoOptional = authorService.read(authorDto);
        return dtoOptional.orElseGet(() -> {
            authorDto.setId(0);
            return authorService.create(authorDto);
        });
    }

    @Override
    @Transactional
    public Optional<NewsDto> read(NewsDto dto) {
        Optional<News> newsOptional = newsRepository.findById(dto.getId());
        return newsOptional.map(this::convertToDto);
    }

    @Override
    @Transactional
    public Optional<NewsDto> update(NewsDto dto) {
        Optional<News> optionalNews = newsRepository.findById(dto.getId());
        if (optionalNews.isPresent()) {
            AuthorDto authorDto = createAuthorIfNotExist(dto.getAuthor());
            Set<TagDto> tagDtoSet = createTagsIfNotExist(dto.getTags());
            dto.setAuthor(authorDto);
            dto.setTags(tagDtoSet);
            News news = newsRepository.update(convertToEntity(dto));
            return Optional.of(convertToDto(news));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void delete(NewsDto dto) {
        Optional<News> newsOptional = newsRepository.findById(dto.getId());
        newsOptional.ifPresent(news -> newsRepository.delete(news));
    }

    private NewsDto convertToDto(News news) {
        return modelMapper.map(news, NewsDto.class);
    }

    private News convertToEntity(NewsDto newsDto) {
        return modelMapper.map(newsDto, News.class);
    }

    @Override
    public List<NewsDto> findBySpecification(List<String> authorsName, List<String> tagsName, List<String> sorts) {
        List<SearchCriteria> criteriaList = new ArrayList<>();
        buildSearchCriteria(authorsName, criteriaList, NewsSearchSpecification.AUTHOR_NAME);
        buildSearchCriteria(tagsName, criteriaList, NewsSearchSpecification.TAGS_NAME);
        if (isCriteriaListNotEmpty(criteriaList)) {
            SearchSpecification<News> searchSpecification = new NewsSpecificationBuilder().with(criteriaList).build();
            return getNewsDtoBySearchAndSortSpec(sorts, searchSpecification);
        }
        return Collections.emptyList();
    }

    @Override
    public List<NewsDto> findAll() {
        return newsRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private boolean isCriteriaListNotEmpty(List<SearchCriteria> criteriaList) {
        return !criteriaList.isEmpty();
    }

    @Override
    public long getCountOfNews() {
        return newsRepository.count();
    }

    private List<NewsDto> getNewsDtoBySearchAndSortSpec(List<String> sorts, SearchSpecification<News> searchSpecification) {
        if (sorts == null || sorts.isEmpty()) {
            return newsRepository.findAll(searchSpecification).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else {
            SortCriteria sortCriteria = new SortCriteria(sorts.get(0));
            NewsSortSpecification sortSpecification = new NewsSortSpecification(sortCriteria);
            return newsRepository.findAll(searchSpecification, sortSpecification).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
    }

    private void buildSearchCriteria(List<String> strings, List<SearchCriteria> criteriaList, String param) {
        if (strings != null && !strings.isEmpty()) {
            strings.forEach(s -> criteriaList.add(new SearchCriteria(param, s)));
        }
    }
}
