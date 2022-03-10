package net.thumbtack.school.store.dao;

import java.util.List;

public interface Dao <T> {
    T findById(String id);
    List<T> findAll();
    void insert(T obj);
    void delete(T obj);
}