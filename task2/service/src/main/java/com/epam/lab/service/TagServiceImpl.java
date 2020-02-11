package com.epam.lab.service;

import com.epam.lab.dto.TagDto;
import com.epam.lab.exception.ServiceException;
import com.epam.lab.model.Tag;
import com.epam.lab.repository.TagRepo;
import com.epam.lab.specification.QuerySpecification;
import com.epam.lab.specification.QueryTagByIdSpec;
import com.epam.lab.specification.QueryTagByNameSpec;
import com.epam.lab.specification.QueryTagsByNewsIdSpec;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
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
    public Optional<TagDto> update(TagDto dto) {
        if (tagRepo.update(convertToTag(dto))) {
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
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
        return false;
    }
}
