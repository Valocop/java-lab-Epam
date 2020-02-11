package com.epam.lab.service;

import com.epam.lab.dto.TagDto;

import java.util.List;

public interface TagService extends Service<TagDto> {
    TagDto findById(long id);
    List<TagDto> findByName(String name);
    List<TagDto> findByNewsId(long id);
    boolean deleteUnsignedTags();
}
