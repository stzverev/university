package ua.com.foxminded.university.data.db.dao;

import java.util.List;

public interface GenericDao <T> {

    void save(List<T> list);

    void save(T object);

    T getById(long id);

    List<T> getAll();

}
