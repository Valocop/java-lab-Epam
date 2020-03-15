package com.epam.lab.service;

import com.epam.lab.dto.TagDto;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<TagDto> update(TagDto dto) {
        Optional<Tag> optionalTag = tagRepository.findById(dto.getId());
        if (optionalTag.isPresent()) {
            Tag tag = tagRepository.update(convertToTag(dto));
            return Optional.of(convertToDto(tag));
        }
        return Optional.empty();
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

    @Override
    public List<TagDto> readAll() {
        return tagRepository.readAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
