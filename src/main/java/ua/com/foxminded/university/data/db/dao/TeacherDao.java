package ua.com.foxminded.university.data.db.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;

import ua.com.foxminded.university.data.model.Teacher;

public interface TeacherDao extends PersonDao<Teacher> {

    @EntityGraph(attributePaths = "courses")
    Optional<Teacher> findFetchCoursesById(long id);

}
