package com.epam.lab.service;

import com.epam.lab.dto.AuthorDto;
import com.epam.lab.repository.AuthorRepo;

public class AuthorServiceImpl implements AuthorService {
    private AuthorRepo authorRepo;

    public AuthorServiceImpl(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    @Override
    public long create(AuthorDto dto) {
        return 0;
    }

    @Override
    public boolean update(AuthorDto dto) {
        return false;
    }

    @Override
    public boolean remove(AuthorDto dto) {
        return false;
    }
}
