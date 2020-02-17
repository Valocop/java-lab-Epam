package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService extends Service<AuthorDto> {
    Optional<AuthorDto> findById(long id);

    Optional<AuthorDto> findByIdAndName(long id, String name);

    List<AuthorDto> findByNewsId(long newsId);
}
