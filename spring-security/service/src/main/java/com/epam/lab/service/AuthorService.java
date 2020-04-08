package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;

import java.util.List;

public interface AuthorService extends Service<AuthorDto> {

    List<AuthorDto> readAll();
}
