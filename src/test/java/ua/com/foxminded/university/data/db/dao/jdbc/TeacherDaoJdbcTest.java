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
import ua.com.foxminded.university.data.model.Teacher;

class TeacherDaoJdbcTest {

    private TeacherDaoJdbc teacherDao;
    private CourseDaoJdbc courseDao;

    @BeforeEach
    private void init() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        DataInitializer dataInitializer =
                context.getBean(DataInitializer.class);
        dataInitializer.loadData();
        teacherDao = context.getBean(TeacherDaoJdbc.class);
        courseDao = context.getBean(CourseDaoJdbc.class);
        context.close();
    }

    @Test
    void shouldGetByNameTeacherWhenSaveTeacher() {
        String firstName = "Sheldon";
        String lastName = "Cooper";
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacherDao.save(teacher);
        Teacher expected = teacher;

        Teacher actual = teacherDao.getByFullName(firstName, lastName);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetListTeachersWhenSaveList() {
        List<Teacher> teachers = new ArrayList<>();
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("Sheldon");
        teacher1.setLastName("Cooper");
        teachers.add(teacher1);
        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Leonard");
        teacher2.setLastName("Hofstadter");
        teachers.add(teacher2);
        teacherDao.save(teachers);

        List<Teacher> actual = teacherDao.getAll();

        assertEquals(teachers, actual);
    }

    @Test
    void shouldGetTeacherByIdWhenSaveTeacher() {
        String firstName = "Curtis";
        String lastName = "Connors";
        Teacher expected = new Teacher();
        expected.setFirstName(firstName);
        expected.setLastName(lastName);
        teacherDao.save(expected);
        expected = teacherDao.getByFullName(firstName, lastName);

        Teacher actual = teacherDao.getById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetCoursesWhenSaveCoursesForTeacher() {
        Course courseMath = saveAndGetCourse("Math");
        Course courseHistory = saveAndGetCourse("History");
        Teacher teacher = saveAndGetTeacher("Sheldon", "Cooper");
        List<Course> courses = new ArrayList<>();
        courses.add(courseMath);
        courses.add(courseHistory);
        teacher.setCourses(courses);
        teacherDao.saveCourses(teacher);
        List<Course> expected = courses;

        List<Course> actual = teacherDao.getCourses(teacher);

        assertEquals(expected, actual);
    }

    private Teacher saveAndGetTeacher(String firstName, String lastName) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacherDao.save(teacher);
        return teacherDao.getByFullName(firstName, lastName);
    }

    private Course saveAndGetCourse(String courseName) {
        Course course = new Course();
        course.setName(courseName);
        courseDao.save(course);
        return courseDao.getByName(courseName);
    }

}
