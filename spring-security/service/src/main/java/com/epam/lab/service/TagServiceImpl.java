package com.epam.lab.service;

import com.epam.lab.dto.TagDto;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.lab.service.ServiceUtil.convertToDto;
import static com.epam.lab.service.ServiceUtil.convertToEntity;

@Service
public class TagServiceImpl implements TagService {
    private TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public TagDto create(TagDto dto) {
        Tag tag = tagRepository.save(convertToEntity(dto));
        return convertToDto(tag);
    }

    @Override
    public Optional<TagDto> read(TagDto dto) {
        Optional<Tag> tagOptional = tagRepository.findById(dto.getId());
        return tagOptional.map(ServiceUtil::convertToDto);
    }

    @Override
    public Optional<TagDto> update(TagDto dto) {
        Optional<Tag> optionalTag = tagRepository.findById(dto.getId());
        if (optionalTag.isPresent()) {
            Tag tag = tagRepository.update(convertToEntity(dto));
            return Optional.of(convertToDto(tag));
        }
        return Optional.empty();
    }

    @Override
    public void delete(TagDto dto) {
        tagRepository.delete(convertToEntity(dto));
    }

    @Override
    public List<TagDto> readAll() {
        return tagRepository.readAll().stream()
                .map(ServiceUtil::convertToDto)
                .collect(Collectors.toList());
    }
}
