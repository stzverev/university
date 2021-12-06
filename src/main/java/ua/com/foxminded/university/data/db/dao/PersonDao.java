package ua.com.foxminded.university.data.db.dao;

import java.io.Serializable;

public interface PersonDao<T extends Serializable> extends GenericDao<T> {

    T getByFullName(String firstName, String lastName);

}
