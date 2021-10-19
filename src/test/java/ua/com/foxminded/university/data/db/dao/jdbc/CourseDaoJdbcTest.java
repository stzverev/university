package ua.com.foxminded.university.data.db.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.DataInitializer;
import ua.com.foxminded.university.data.model.Course;

@SpringJUnitConfig(Config.class)
class CourseDaoJdbcTest {

    @Autowired
    private CourseDaoJdbc courseDao;

    @Autowired
    private  DataInitializer dataInitializer;

    @BeforeEach
    private void init() {
        dataInitializer.loadData();
    }

    @Test
    void shouldGetByNameCourseWhenSaveCourse() {
        String name = "Math";
        Course course = new Course();
        course.setName(name);
        courseDao.save(course);
        Course expected = course;

        Course actual = courseDao.getByName(name);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetByNameCourseWhenUpdateCourse() {
        String name = "Math";
        String newName = "Math2";
        Course course = new Course();
        course.setName(name);
        courseDao.save(course);
        course = courseDao.getByName(name);
        course.setName(newName);
        courseDao.update(course);
        Course expected = course;

        Course actual = courseDao.getByName(newName);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetListCoursesWhenSaveList() {
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course();
        course1.setName("Math");
        courses.add(course1);
        Course course2 = new Course();
        course2.setName("History");
        courses.add(course2);
        courseDao.save(courses);

        List<Course> actual = courseDao.getAll();

        assertEquals(courses, actual);
    }

    @Test
    void shouldGetCourseByIdWhenSaveCourse() {
        String name = "CIA";
        Course expected = new Course();
        expected.setName(name);
        courseDao.save(expected);
        expected = courseDao.getByName(name);

        Course actual = courseDao.getById(expected.getId());

        assertEquals(expected, actual);
    }

}
