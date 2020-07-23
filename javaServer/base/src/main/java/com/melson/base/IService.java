package com.melson.base;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IService<T> {

    JpaRepository<T,String> getRepository();

    T insert(T t);

    void delete(String id);

    T update(T t);

    List<T> batchUpdate(List<T> list);

    T get(String id);

    List<T> getAll();
}
