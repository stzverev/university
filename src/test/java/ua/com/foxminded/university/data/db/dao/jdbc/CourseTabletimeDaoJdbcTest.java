package ua.com.foxminded.university.data.db.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.DataInitializer;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Tabletime;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;

class CourseTabletimeDaoJdbcTest {

    private CourseTabletimeDaoJdbc courseTabletimeDaoJdbc;
    private CourseDaoJdbc courseDaoJdbc;
    private GroupDaoJdbc groupDaoJdbc;
    private TeacherDaoJdbc teacherDaoJdbc;

    @BeforeEach
    private void init() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        DataInitializer dataInitializer =
                context.getBean(DataInitializer.class);
        dataInitializer.loadData();
        courseTabletimeDaoJdbc =
                context.getBean(CourseTabletimeDaoJdbc.class);
        courseDaoJdbc = context.getBean(CourseDaoJdbc.class);
        groupDaoJdbc = context.getBean(GroupDaoJdbc.class);
        teacherDaoJdbc = context.getBean(TeacherDaoJdbc.class);
        context.close();
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
        Tabletime tabletime = new Tabletime(dateTime, dateTime, rows);
        courseTabletimeDaoJdbc.saveTabletime(tabletime);
        List<TabletimeRow> expected = tabletime.getRows();

        LocalDateTime begin = LocalDateTime.of(2021, 10, 11, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2021, 10, 11, 23, 59, 59);
        Tabletime tabletimeGet = courseTabletimeDaoJdbc.getTabletime(course,
                begin, end);
        List<TabletimeRow> actual = tabletimeGet.getRows();

        assertEquals(expected, actual);
    }

    private Teacher saveAndGetTeacher(String firstName, String lastName) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacherDaoJdbc.save(teacher);
        return teacherDaoJdbc.getByFullName(firstName, lastName);
    }

    private Group saveAndGetGroup(String name) {
        Group group = new Group();
        group.setName(name);
        groupDaoJdbc.save(group);
        return groupDaoJdbc.getByName(name);
    }

    private Course saveAndGetCourse(String name) {
        Course course = new Course();
        course.setName(name);
        courseDaoJdbc.save(course);
        return courseDaoJdbc.getByName(name);
    }

}
