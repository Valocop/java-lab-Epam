package com.epam.lab.dao;

import java.util.List;

public interface CRUDDao<ENTITY, KEY> {
    KEY save(ENTITY entity);
    boolean update(ENTITY entity);
    boolean delete(ENTITY entity);
    ENTITY getById(KEY id);
    List<ENTITY> findAll();
}
