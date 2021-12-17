package ua.com.foxminded.university.data.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;

import ua.com.foxminded.university.data.model.Teacher;

public interface TeacherRepository extends PersonRepository<Teacher> {

    @EntityGraph(attributePaths = "courses")
    Optional<Teacher> findFetchCoursesById(long id);

}
