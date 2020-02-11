package com.epam.lab.service;

import java.util.Optional;

public interface Service<T> {
    long create(T dto);

    Optional<T> update(T dto);

    boolean remove(T dto);
}
