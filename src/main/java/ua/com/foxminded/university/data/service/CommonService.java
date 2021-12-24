package ua.com.foxminded.university.data.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import ua.com.foxminded.university.data.model.AbstractEntity;

public interface CommonService<T extends AbstractEntity> {

    void save(T object);

    void save(List<T> objects);

    void deleteById(long id);

    Page<T> findAll(PageRequest pageRequest);

    List<T> findAll();

    T findById(long id);

}
