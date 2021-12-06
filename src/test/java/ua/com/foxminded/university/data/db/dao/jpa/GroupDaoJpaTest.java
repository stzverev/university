package ua.com.foxminded.university.data.db.dao.jpa;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.ConfigTest;
import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.db.dao.StudentDao;
import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.DataInitializer;

@SpringJUnitConfig(ConfigTest.class)
class GroupDaoJpaTest {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private DataInitializer dataInitializer;

    @BeforeEach
    private void init() {
        dataInitializer.loadData();
    }

    @Test
    void shouldGetByNameGroupWhenSaveGroup() {
        String name = "MI6";
        Group group = new Group();
        group.setName(name);
        groupDao.save(group);
        Group expected = group;

        Group actual = groupDao.getByName(name);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetByNameGroupWhenUpdateGroup() {
        String name = "MI6";
        String newName = "MI7";
        Group group = new Group();
        group.setName(name);
        groupDao.save(group);
        group = groupDao.getByName(name);
        group.setName(newName);
        groupDao.update(group);
        Group expected = group;

        Group actual = groupDao.getByName(newName);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetListGroupsWhenSaveList() {
        List<Group> groups = new ArrayList<>();
        Group group1 = new Group();
        group1.setName("MI6");
        groups.add(group1);
        Group group2 = new Group();
        group2.setName("KGB");
        groups.add(group2);
        groupDao.save(groups);

        List<Group> actual = groupDao.getAll();

        assertEquals(groups, actual);
    }

    @Test
    void shouldGetGroupByIdWhenSaveGroup() {
        String name = "CIA";
        Group expected = new Group();
        expected.setName(name);
        groupDao.save(expected);
        expected = groupDao.getByName(name);

        Group actual = groupDao.getById(expected.getId()).get();

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetStudentsWhenSaveStudentToGroupThenOk() {
        String groupName = "Elementary physics";
        Group group = new Group();
        group.setName(groupName);
        groupDao.save(group);
        group = groupDao.getByName(groupName);
        Student student = new Student();
        student.setFirstName("Albert");
        student.setLastName("Einstein");
        student.setGroup(group);
        studentDao.save(student);
        Set<Student> expected = Collections.singleton(student);

        Set<Student> actual = groupDao.getStudents(group);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetTabletimeForGroupWhenSaveTabletimeThenOk() {
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
        groupDao.addTabletimeRows(rows);
        Set<TabletimeRow> expected = rows;

        LocalDateTime begin = LocalDateTime.of(2021, 10, 11, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2021, 10, 11, 23, 59, 59);
        Set<TabletimeRow> tabletimeRowsGet = groupDao.getTabletime(group,
                begin, end);
        Set<TabletimeRow> actual = tabletimeRowsGet;

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetTabletimeForGroupWhenUpdateTabletimeThenOk() {
        Course course = saveAndGetCourse("Math");
        Group group = saveAndGetGroup("FJ-42");
        Teacher teacher = saveAndGetTeacher("Sheldon", "Cooper");
        saveAndGetTabletimeRow(
                LocalDateTime.of(2021, 10, 11, 9, 0),
                course, group, teacher);
        Set<TabletimeRow> expected = groupDao.getTabletime(group,
                LocalDateTime.of(2021, 10, 11, 0, 0, 0),
                LocalDateTime.of(2021, 10, 11, 23, 59, 59));
        expected.stream().findFirst().get().setDateTime(
                LocalDateTime.of(2021, 10, 12, 10, 0));
        groupDao.updateTabletime(expected);
        Set<TabletimeRow> actual = groupDao.getTabletime(group,
                LocalDateTime.of(2021, 10, 12, 0, 0, 0),
                LocalDateTime.of(2021, 10, 12, 23, 59, 59));;

        assertEquals(expected, actual);
    }

    @Test
    void shouldDidntFindCourseWhenGetCoursesForGroupAfterRemove() {
        Course course = saveAndGetCourse("Phisycs");
        Group group = saveAndGetGroup("RR-25");
        groupDao.addToCourses(group, Collections.singleton(course));
        groupDao.deleteFromCourse(group, course);

        Set<Course> courses = groupDao.getCourses(group);

        assertThat(courses, not(hasItem(course)));
    }

    @Test
    void shouldThrowExceptionWhenGetGroupThatWasDeleted() {
        Group group = saveAndGetGroup("RR-255");
        long groupId = group.getId();
        groupDao.delete(groupId);

        Throwable error = assertThrows(DataAccessException.class, () -> groupDao.getById(groupId));
        assertEquals("Incorrect result size: expected 1, actual 0", error.getMessage());
    }

    private Set<TabletimeRow> saveAndGetTabletimeRow(LocalDateTime dateTime, Course course, Group group,
            Teacher teacher) {
        TabletimeRow row = new TabletimeRow();
        row.setTeacher(teacher);
        row.setCourse(course);
        row.setGroup(group);
        row.setDateTime(dateTime);
        Set<TabletimeRow> rows = Collections.singleton(row);
        groupDao.addTabletimeRows(rows);
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
