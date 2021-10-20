package ua.com.foxminded.university.data.db.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.DataInitializer;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;

@SpringJUnitConfig(Config.class)
class CourseDaoJdbcTest {

    @Autowired
    private CourseDaoJdbc courseDao;

    @Autowired
    private TeacherDaoJdbc teacherDao;

    @Autowired
    private GroupDaoJdbc groupDao;

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

    @Test
    void shouldGetTabletimeForCourseWhenSaveTabletimeThenOk() {
        LocalDateTime dateTime = LocalDateTime.of(2021, 10, 11, 9, 0);
        Course course = saveAndGetCourse("Math");
        Group group = saveAndGetGroup("FJ-42");
        Teacher teacher = saveAndGetTeacher("Sheldon", "Cooper");
        TabletimeRow row = new TabletimeRow();
        row.setTeacher(teacher);
        row.setCourse(course);
        row.setGroup(group);
        row.setDateTime(dateTime);
        List<TabletimeRow> rows = Collections.singletonList(row);
        courseDao.saveTabletime(rows);
        List<TabletimeRow> expected = rows;

        LocalDateTime begin = LocalDateTime.of(2021, 10, 11, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2021, 10, 11, 23, 59, 59);
        List<TabletimeRow> tabletimeRowsGet = courseDao.getTabletime(course,
                begin, end);
        List<TabletimeRow> actual = tabletimeRowsGet;

        assertEquals(expected, actual);
    }

    private Teacher saveAndGetTeacher(String firstName, String lastName) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacherDao.save(teacher);
        return teacherDao.getByFullName(firstName, lastName);
    }

    private Group saveAndGetGroup(String name) {
        Group group = new Group();
        group.setName(name);
        groupDao.save(group);
        return groupDao.getByName(name);
    }

    private Course saveAndGetCourse(String name) {
        Course course = new Course();
        course.setName(name);
        courseDao.save(course);
        return courseDao.getByName(name);
    }

}
