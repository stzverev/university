package ua.com.foxminded.university.data.db.dao.jpa;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.ConfigTest;
import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.DataInitializer;

@SpringJUnitConfig(ConfigTest.class)
class TeacherDaoJpaTest {

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private DataInitializer dataInitializer;

    @BeforeEach
    private void init() {
        dataInitializer.loadData();
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
    void shouldGetByNameTeacherWhenUpdateTeacher() {
        String firstName = "Sheldon";
        String lastName = "Cooper";
        String newFirstName = "Sh2";
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacherDao.save(teacher);
        teacher = teacherDao.getByFullName(firstName, lastName);
        teacher.setFirstName(newFirstName);
        teacherDao.update(teacher);
        Teacher expected = teacher;

        Teacher actual = teacherDao.getByFullName(newFirstName, lastName);

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

        Teacher actual = teacherDao.getById(expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetCoursesWhenSaveCoursesForTeacher() {
        Course courseMath = saveAndGetCourse("Math");
        Course courseHistory = saveAndGetCourse("History");
        Teacher teacher = saveAndGetTeacher("Sheldon", "Cooper");
        Set<Course> courses = new HashSet<>();
        courses.add(courseMath);
        courses.add(courseHistory);
        teacherDao.addToCourses(teacher, courses);
        Set<Course> expected = courses;

        Set<Course> actual = teacherDao.getCourses(teacher);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetTabletimeForTeacherWhenSaveTabletimeThenOk() {
        LocalDateTime dateTime = LocalDateTime.of(2021, 10, 11, 9, 0);
        Course course = saveAndGetCourse("Math");
        Group group = saveAndGetGroup("FJ-42");
        Teacher teacher = saveAndGetTeacher("Sheldon", "Cooper");
        TabletimeRow row = new TabletimeRow();
        row.setTeacher(teacher);
        row.setCourse(course);
        row.setGroup(group);
        row.setDateTime(dateTime);
        Set<TabletimeRow> rows = Collections.singleton(row);
        teacherDao.addTabletimeRows(rows);
        Set<TabletimeRow> expected = rows;

        LocalDateTime begin = LocalDateTime.of(2021, 10, 11, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2021, 10, 11, 23, 59, 59);
        Set<TabletimeRow> tabletimeRowsGet = teacherDao.getTabletime(teacher,
                begin, end);
        Set<TabletimeRow> actual = tabletimeRowsGet;

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetTabletimeForTeacherWhenUpdateTabletimeThenOk() {
        Course course = saveAndGetCourse("Math");
        Group group = saveAndGetGroup("FJ-42");
        Teacher teacher = saveAndGetTeacher("Sheldon", "Cooper");
        saveAndGetTabletimeRow(
                LocalDateTime.of(2021, 10, 11, 9, 0),
                course, group, teacher);
        Set<TabletimeRow> expected = teacherDao.getTabletime(teacher,
                LocalDateTime.of(2021, 10, 11, 0, 0, 0),
                LocalDateTime.of(2021, 10, 11, 23, 59, 59));
        expected.stream()
            .findFirst()
            .get()
            .setDateTime(
                LocalDateTime.of(2021, 10, 12, 10, 0));
        teacherDao.updateTabletime(expected);
        Set<TabletimeRow> actual = teacherDao.getTabletime(teacher,
                LocalDateTime.of(2021, 10, 12, 0, 0, 0),
                LocalDateTime.of(2021, 10, 12, 23, 59, 59));;

        assertEquals(expected, actual);
    }

    @Test
    void shouldDidntFindCourseWhenTryGetCoursesForTeacherAfterRemove() {
        Teacher teacher = saveAndGetTeacher("Sheldon", "Cooper");
        Course course = saveAndGetCourse("Physics");
        teacherDao.addToCourses(teacher, Collections.singleton(course));
        teacherDao.removeCourse(teacher, course);
        Set<Course> teacherCourses = teacherDao.getCourses(teacher);

        assertThat(teacherCourses, not(hasItem(course)));
    }

    private Set<TabletimeRow> saveAndGetTabletimeRow(LocalDateTime dateTime, Course course, Group group,
            Teacher teacher) {
        TabletimeRow row = new TabletimeRow();
        row.setTeacher(teacher);
        row.setCourse(course);
        row.setGroup(group);
        row.setDateTime(dateTime);
        Set<TabletimeRow> rows = Collections.singleton(row);
        teacherDao.addTabletimeRows(rows);
        return rows;
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

    private Course saveAndGetCourse(String courseName) {
        Course course = new Course();
        course.setName(courseName);
        courseDao.save(course);
        return courseDao.getByName(courseName);
    }



}
