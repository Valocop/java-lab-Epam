package com.epam.lab.repository;

import com.epam.lab.specification.FindSpecification;

import java.util.List;

public interface Repository<ENTITY, KEY> {
    KEY save(ENTITY entity);

    boolean update(ENTITY entity);

    boolean delete(ENTITY entity);

    List<ENTITY> findBy(FindSpecification spec);

    List<ENTITY> findAll();
}
