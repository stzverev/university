package ua.com.foxminded.university.data.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ua.com.foxminded.university.data.model.Person;

@NoRepositoryBean
public interface PersonRepository<T extends Person> extends JpaRepository<T, Long> {

    Optional<T> findByFirstNameAndLastName(String firstName, String lastName);

}
