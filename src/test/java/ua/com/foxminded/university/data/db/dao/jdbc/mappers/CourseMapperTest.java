package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.model.Course;

@SpringJUnitConfig(Config.class)
@ExtendWith(MockitoExtension.class)
class CourseMapperTest {

    private CourseMapper courseMapper;

    @Autowired
    private void setCourseMapper(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Test
    void shouldGetCourse() throws SQLException {
        Course course = new Course();
        course.setName("Math");
        course.setId(1);
        Course expected = course;

        ResultSet resultSet = getCourseResultSetMock(course);
        Course actual = courseMapper.mapRow(resultSet, 1);

        assertEquals(expected, actual);
    }

    private ResultSet getCourseResultSetMock(Course course) throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getLong("course_id")).thenReturn(course.getId());
        when(resultSet.getString("course_name")).thenReturn(course.getName());
        return resultSet;
    }

}
