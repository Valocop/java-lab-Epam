package com.epam.lab.service;

import org.modelmapper.ModelMapper;

import java.util.Optional;

public interface Service<T> {
    long create(T dto);

    Optional<T> update(T dto);

    boolean remove(T dto);

    void setModelMapper(ModelMapper modelMapper);
}
