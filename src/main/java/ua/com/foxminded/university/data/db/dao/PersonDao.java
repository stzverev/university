package ua.com.foxminded.university.data.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ua.com.foxminded.university.data.model.Person;

@NoRepositoryBean
public interface PersonDao<T extends Person> extends JpaRepository<T, Long> {

    T findByFirstNameAndLastName(String firstName, String lastName);

}
