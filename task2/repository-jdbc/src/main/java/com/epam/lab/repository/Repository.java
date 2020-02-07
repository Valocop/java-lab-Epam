package com.epam.lab.repository;

import com.epam.lab.specification.QuerySpecification;

import java.util.List;

public interface Repository<ENTITY, KEY> {
    KEY save(ENTITY entity);
    boolean update(ENTITY entity);
    boolean delete(ENTITY entity);
    List<ENTITY> find(QuerySpecification spec);
    List<ENTITY> findAll();
}
