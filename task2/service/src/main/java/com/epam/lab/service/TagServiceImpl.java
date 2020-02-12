package com.epam.lab.service;

import com.epam.lab.dto.NewsDto;
import com.epam.lab.dto.TagDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.NewsRepo;
import com.epam.lab.repository.TagRepo;
import com.epam.lab.specification.*;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TagServiceImpl implements TagService {
    private ModelMapper modelMapper;
    private TagRepo tagRepo;
    private NewsRepo newsRepo;

    public TagServiceImpl(TagRepo tagRepo, NewsRepo newsRepo) {
        this.tagRepo = tagRepo;
        this.newsRepo = newsRepo;
    }

    @Override
    public long create(TagDto dto) {
        return tagRepo.save(convertToTag(dto));
    }

    @Override
    public Optional<TagDto> update(TagDto dto) {
        if (tagRepo.update(convertToTag(dto))) {
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean remove(TagDto dto) {
        List<NewsDto> newsDtoList = newsRepo.find(new QueryNewsByTagIdSpec(dto.getId())).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        newsDtoList.forEach(newsDto -> {
            News news = new News();
            news.setId(newsDto.getId());
            Tag tag = new Tag();
            tag.setId(dto.getId());
            if (!newsRepo.deleteTagOfNews(news, tag)) {
                throw new ServiceException("Failed to delete relation of tag "
                        + dto.getId() + " news " + newsDto.getId());
            }
        });
        return tagRepo.delete(convertToTag(dto));
    }

    private TagDto convertToDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }

    private Tag convertToTag(TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }

    private NewsDto convertToDto(News news) {
        return modelMapper.map(news, NewsDto.class);
    }

    private News convertToEntity(NewsDto newsDto) {
        return modelMapper.map(newsDto, News.class);
    }

    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDto findById(long id) {
        QuerySpecification findByIdSpec = new QueryTagByIdSpec(id);
        List<TagDto> tagDtoList = tagRepo.find(findByIdSpec)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        if (tagDtoList.isEmpty()) {
            throw new ServiceException("Fail to find tag by id. Not founded");
        }
        return tagDtoList.get(0);
    }

    @Override
    public List<TagDto> findByName(String name) {
        QuerySpecification spec = new QueryTagByNameSpec(name);
        return tagRepo.find(spec).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TagDto> findByNewsId(long newsId) {
        return tagRepo.find(new QueryTagsByNewsIdSpec(newsId)).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteUnsignedTags() {
        return tagRepo.deleteUnsignedTags();
    }
}
