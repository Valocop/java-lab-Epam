package com.epam.lab.service;

import java.util.Optional;

public interface Service<T> {

    T create(T dto);

    Optional<T> read(T dto);

    Optional<T> update(T dto);

    void delete(T dto);
}
