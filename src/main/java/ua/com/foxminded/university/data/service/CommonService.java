package ua.com.foxminded.university.data.service;

import java.util.List;

import ua.com.foxminded.university.data.model.AbstractEntity;

public interface CommonService<T extends AbstractEntity> {

    void save(T object);

    void save(List<T> objects);

    void deleteById(long id);

    List<T> findAll();

    T findById(long id);

}
