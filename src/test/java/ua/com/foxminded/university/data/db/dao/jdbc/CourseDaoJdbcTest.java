package ua.com.foxminded.university.data.db.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.DataInitializer;
import ua.com.foxminded.university.data.model.Course;

class CourseDaoJdbcTest {

    private CourseDaoJdbc courseDao;

    @BeforeEach
    private void init() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        DataInitializer dataInitializer =
                context.getBean(DataInitializer.class);
        dataInitializer.loadData();
        courseDao = context.getBean(CourseDaoJdbc.class);
        context.close();
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
