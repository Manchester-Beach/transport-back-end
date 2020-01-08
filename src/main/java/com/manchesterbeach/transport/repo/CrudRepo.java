package com.manchesterbeach.transport.repo;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CrudRepo<T> {
    List<T> findAll();

    T findById(Long id);

    void save(T object);

    ResponseEntity delete(T object);

    void deleteById(Long id);

    int count();
}
