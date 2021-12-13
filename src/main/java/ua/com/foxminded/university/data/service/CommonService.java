package ua.com.foxminded.university.data.service;

import java.util.List;

import ua.com.foxminded.university.data.model.AbstractEntity;

public interface CommonService<T extends AbstractEntity> {

    void save(T object);

    void save(List<T> objects);

    void delete(long id);

    List<T> getAll();

    T getById(long id);

}
