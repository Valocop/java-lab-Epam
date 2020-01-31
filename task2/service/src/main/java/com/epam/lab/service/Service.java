package com.epam.lab.service;

import com.epam.lab.repository.QuerySpecification;

import java.util.List;

public interface Service<T> {
    List<T> read(QuerySpecification spec);
    long create(T entity);
    boolean update(T entity);
    boolean remove(T entity);
}
