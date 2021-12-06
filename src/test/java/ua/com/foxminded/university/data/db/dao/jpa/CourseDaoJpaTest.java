package ua.com.foxminded.university.data.db.dao.jpa;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
class CourseDaoJpaTest {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private DataInitializer dataInitializer;

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

        Course actual = courseDao.getById(expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetTeachersAfterAddTeacherToCourse() {
        Course course = saveAndGetCourse("Phisycs");
        Teacher teacher = saveAndGetTeacher("Sheldon", "Cooper");
        teacherDao.addToCourses(teacher, Collections.singleton(course));

        Set<Teacher> teachers = courseDao.getTeachers(course);

        assertThat(teachers, hasItem(teacher));
    }

    @Test
    void shouldGetGroupsAfterAddGroupToCourse() {
        Course course = saveAndGetCourse("Phisycs");
        Group group = saveAndGetGroup("GE-22");
        groupDao.addToCourses(group, Collections.singleton(course));

        Set<Group> groups = courseDao.getGroups(course);

        assertThat(groups, hasItem(group));
    }

    @Test
    void shouldGetTabletimeForCourseWhenSaveTabletimeThenOk() {
        LocalDateTime dateTime = LocalDateTime.of(2021, 10, 11, 9, 0);
        Course course = saveAndGetCourse("Math");
        Group group = saveAndGetGroup("FJ-42");
        Teacher teacher = saveAndGetTeacher("Sheldon", "Cooper");
        List<TabletimeRow> rows = saveAndGetTabletimeRow(dateTime, course, group, teacher);
        List<TabletimeRow> expected = rows;

        LocalDateTime begin = LocalDateTime.of(2021, 10, 11, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2021, 10, 11, 23, 59, 59);
        List<TabletimeRow> tabletimeRowsGet = courseDao.getTabletime(course,
                begin, end);
        List<TabletimeRow> actual = tabletimeRowsGet;

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetTabletimeForCourseWhenUpdateTabletimeThenOk() {
        Course course = saveAndGetCourse("Math");
        Group group = saveAndGetGroup("FJ-42");
        Teacher teacher = saveAndGetTeacher("Sheldon", "Cooper");
        saveAndGetTabletimeRow(
                LocalDateTime.of(2021, 10, 11, 9, 0),
                course, group, teacher);
        List<TabletimeRow> expected = courseDao.getTabletime(course,
                LocalDateTime.of(2021, 10, 11, 0, 0, 0),
                LocalDateTime.of(2021, 10, 11, 23, 59, 59));
        expected.get(0).setDateTime(
                LocalDateTime.of(2021, 10, 12, 10, 0));
        courseDao.updateTabletime(expected);
        List<TabletimeRow> actual = courseDao.getTabletime(course,
                LocalDateTime.of(2021, 10, 12, 0, 0, 0),
                LocalDateTime.of(2021, 10, 12, 23, 59, 59));;

        assertEquals(expected, actual);
    }

    private List<TabletimeRow> saveAndGetTabletimeRow(LocalDateTime dateTime, Course course, Group group,
            Teacher teacher) {
        TabletimeRow row = new TabletimeRow();
        row.setTeacher(teacher);
        row.setCourse(course);
        row.setGroup(group);
        row.setDateTime(dateTime);
        List<TabletimeRow> rows = Collections.singletonList(row);
        courseDao.saveTabletime(rows);
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

    private Course saveAndGetCourse(String name) {
        Course course = new Course();
        course.setName(name);
        courseDao.save(course);
        return courseDao.getByName(name);
    }

}
