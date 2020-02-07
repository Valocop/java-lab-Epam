package com.epam.lab.service;

public interface Service<T> {
    long create(T dto);

    boolean update(T dto);

    boolean remove(T dto);
}
