package com.epam.lab.service;

import com.epam.lab.dto.TagDto;

import java.util.List;

public interface TagService extends Service<TagDto> {

    List<TagDto> readAll();
}
