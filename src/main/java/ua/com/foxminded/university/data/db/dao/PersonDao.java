package ua.com.foxminded.university.data.db.dao;

public interface PersonDao<T> extends GenericDao<T> {

    T getByFullName(String firstName, String lastName);

}
