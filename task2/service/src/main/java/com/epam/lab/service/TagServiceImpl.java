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
        List<NewsDto> newsDtoList = newsRepo.findBy(new FindNewsByTagIdSpec(dto.getId())).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        newsDtoList.forEach(newsDto -> {
            News news = new News();
            news.setId(newsDto.getId());
            Tag tag = new Tag();
            tag.setId(dto.getId());
            if (!newsRepo.deleteBindingOfNewsAndTag(news, tag)) {
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

    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDto findById(long id) {
        FindSpecification findByIdSpec = new FindTagByIdSpec(id);
        List<TagDto> tagDtoList = tagRepo.findBy(findByIdSpec)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        if (tagDtoList.isEmpty()) {
            throw new ServiceException("Fail to find tag by id. Not found");
        }
        return tagDtoList.get(0);
    }

    @Override
    public List<TagDto> findByName(String name) {
        FindSpecification spec = new FindTagByNameSpec(name);
        return tagRepo.findBy(spec).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<TagDto> findByNewsId(long newsId) {
        return tagRepo.findBy(new FindTagsByNewsIdSpec(newsId)).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteUnsignedTags() {
        return tagRepo.deleteUnsignedTags();
    }
}
