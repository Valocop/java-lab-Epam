package com.epam.lab.service;

import com.epam.lab.dto.TagDto;

public interface TagService extends Service<TagDto> {
    TagDto findById(long id);
}
