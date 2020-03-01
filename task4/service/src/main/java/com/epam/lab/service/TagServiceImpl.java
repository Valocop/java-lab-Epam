package com.epam.lab.service;

import com.epam.lab.dto.TagDto;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;
    private ModelMapper modelMapper;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDto create(TagDto dto) {
        Tag tag = tagRepository.save(convertToTag(dto));
        return convertToDto(tag);
    }

    @Override
    public Optional<TagDto> read(TagDto dto) {
        Optional<Tag> tagOptional = tagRepository.findById(dto.getId());
        return tagOptional.map(this::convertToDto);
    }

    @Override
    public TagDto update(TagDto dto) {
        Tag tag = tagRepository.update(convertToTag(dto));
        return convertToDto(tag);
    }

    @Override
    public void delete(TagDto dto) {
        tagRepository.delete(convertToTag(dto));
    }

    private TagDto convertToDto(Tag tag) {
        return modelMapper.map(tag, TagDto.class);
    }

    private Tag convertToTag(TagDto tagDto) {
        return modelMapper.map(tagDto, Tag.class);
    }
}
