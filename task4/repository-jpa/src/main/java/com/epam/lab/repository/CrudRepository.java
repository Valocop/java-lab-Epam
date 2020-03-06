package com.epam.lab.repository;

import java.util.Optional;

public interface CrudRepository<T, ID> {

    T save(T entity);

    T update(T entity);

    Optional<T> findById(ID id);

    void delete(T entity);
}
