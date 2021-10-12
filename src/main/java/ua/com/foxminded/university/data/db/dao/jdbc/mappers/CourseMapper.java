package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Course;

@Component
public class CourseMapper implements RowMapper<Course> {

    @Override
    public Course mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Course course = new Course();
        course.setId(resultSet.getLong("course_id"));
        course.setName(resultSet.getString("course_name"));
        return course;
    }

}
