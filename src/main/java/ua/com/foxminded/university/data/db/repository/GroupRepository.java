package ua.com.foxminded.university.data.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.data.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);

    @EntityGraph(attributePaths = "students")
    Optional<Group> findFetchStudentsById(long id);

    @EntityGraph(attributePaths = "courses")
    Optional<Group> findFetchCoursesById(long id);

}
