package com.epam.lab.repository;

import com.epam.lab.model.Author;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Long> {

    List<Author> readAll();
}
