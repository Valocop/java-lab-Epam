package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.AuthorRepository;
import com.epam.lab.repository.NewsRepository;
import com.epam.lab.repository.TagRepository;
import com.epam.lab.specification.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.epam.lab.service.ServiceUtil.convertToDto;
import static com.epam.lab.service.ServiceUtil.convertToEntity;

@Service
public class NewsServiceImpl implements NewsService {
    private NewsRepository newsRepository;
    private AuthorRepository authorRepository;
    private TagRepository tagRepository;

    @Autowired
    public NewsServiceImpl(@Qualifier("newsRepository") NewsRepository newsRepository, AuthorRepository authorRepository,
                           TagRepository tagRepository) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
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
            Optional<Tag> tagOptional = tagRepository.findById(tagDto.getId());
            Optional<TagDto> dtoOptional = tagOptional.map(ServiceUtil::convertToDto);
            TagDto dto = dtoOptional.orElseGet(() -> {
                tagDto.setId(0);
                Tag savedTag = tagRepository.save(ServiceUtil.convertToEntity(tagDto));
                return convertToDto(savedTag);
            });
            tagsDto.add(dto);
        }
        return tagsDto;
    }

    private AuthorDto createAuthorIfNotExist(AuthorDto authorDto) {
        Optional<Author> authorOptional = authorRepository.findById(authorDto.getId());
        Optional<AuthorDto> dtoOptional = authorOptional.map(ServiceUtil::convertToDto);
        return dtoOptional.orElseGet(() -> {
            authorDto.setId(0);
            Author savedAuthor = authorRepository.save(convertToEntity(authorDto));
            return convertToDto(savedAuthor);
        });
    }

    @Override
    @Transactional
    public Optional<NewsDto> read(NewsDto dto) {
        Optional<News> newsOptional = newsRepository.findById(dto.getId());
        return newsOptional.map(ServiceUtil::convertToDto);
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

    @Override
    public List<NewsDto> findNews(List<String> authorsName, List<String> tagsName, List<String> sorts,
                                  Integer count, Integer page) {
        List<SearchCriteria> criteriaList = new ArrayList<>();
        buildSearchCriteria(authorsName, criteriaList, NewsSearchSpecification.AUTHOR_NAME);
        buildSearchCriteria(tagsName, criteriaList, NewsSearchSpecification.TAGS_NAME);
        SortCriteria sortCriteria = new SortCriteria(sorts.get(0));
        NewsSortSpecification sortSpecification = new NewsSortSpecification(sortCriteria);

        if (criteriaList.isEmpty()) {
            return newsRepository.findAll(sortSpecification, count, page).stream()
                    .map(ServiceUtil::convertToDto)
                    .collect(Collectors.toList());
        } else {
            SearchSpecification<News> searchSpecification = new NewsSpecificationBuilder().with(criteriaList).build();
            return newsRepository.findAll(searchSpecification, sortSpecification, count, page).stream()
                    .map(ServiceUtil::convertToDto)
                    .collect(Collectors.toList());
        }
    }

    private void buildSearchCriteria(List<String> strings, List<SearchCriteria> criteriaList, String param) {
        if (strings != null && !strings.isEmpty()) {
            strings.forEach(s -> criteriaList.add(new SearchCriteria(param, s)));
        }
    }

    @Override
    public long getCountOfNews() {
        return newsRepository.count();
    }

    @Override
    public long getCountOfNews(List<String> authorsName, List<String> tagsName) {
        List<SearchCriteria> criteriaList = new ArrayList<>();
        buildSearchCriteria(authorsName, criteriaList, NewsSearchSpecification.AUTHOR_NAME);
        buildSearchCriteria(tagsName, criteriaList, NewsSearchSpecification.TAGS_NAME);

        if (criteriaList.isEmpty()) {
            return getCountOfNews();
        } else {
            SearchSpecification<News> searchSpecification = new NewsSpecificationBuilder().with(criteriaList).build();
            return newsRepository.count(searchSpecification);
        }
    }
}
