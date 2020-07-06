package com.epam.lab.repository;

import com.epam.lab.model.Tag;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {

    List<Tag> readAll();
}
