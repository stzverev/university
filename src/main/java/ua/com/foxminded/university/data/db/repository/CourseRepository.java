package ua.com.foxminded.university.data.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.data.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByName(String name);

    @EntityGraph(attributePaths = "groups", type = EntityGraphType.FETCH)
    Optional<Course> findFetchGroupsById(Long id);

    @EntityGraph(attributePaths = "teachers", type = EntityGraphType.FETCH)
    Optional<Course> findFetchTeachersById(Long id);

}
