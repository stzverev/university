package ua.com.foxminded.university.data.db.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericDao <T extends Serializable> {

    void save(List<T> list);

    void save(T object);

    void update(T object);

    void delete(long id);

    Optional<T> getById(long id);

    List<T> getAll();

}
