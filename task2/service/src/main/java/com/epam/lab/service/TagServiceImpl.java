package com.epam.lab.service;

import com.epam.lab.dto.TagDto;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.TagRepo;
import com.epam.lab.specification.QuerySpecification;
import com.epam.lab.specification.QueryTagByIdSpec;
import org.modelmapper.ModelMapper;

import java.util.stream.Collectors;

public class TagServiceImpl implements TagService {
    private ModelMapper modelMapper;
    private TagRepo tagRepo;

    public TagServiceImpl(TagRepo tagRepo) {
        this.tagRepo = tagRepo;
    }

    @Override
    public long create(TagDto dto) {
        return tagRepo.save(convertToTag(dto));
    }

    @Override
    public boolean update(TagDto dto) {
        return tagRepo.update(convertToTag(dto));
    }

    @Override
    public boolean remove(TagDto dto) {
        return tagRepo.delete(convertToTag(dto));
    }

    private TagDto convertToDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }

    private Tag convertToTag(TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }

    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDto findById(long id) {
        QuerySpecification findByIdSpec = new QueryTagByIdSpec(id);
        return tagRepo.find(findByIdSpec)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList())
                .get(0);
    }
}
