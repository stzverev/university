package ua.com.foxminded.university.data.service.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.ConfigTest;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.StudentService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundException;

@SpringJUnitConfig(ConfigTest.class)
@Sql(scripts = "classpath:data.sql")
class GroupServiceImplTest {

    private static final String STUDENT_FIRST_NAME = "TEST";
    private static final String STUDENT_LAST_NAME = "STUDENT";
    private static final String COURSE_NAME = "TEST COURSE";
    private static final String GROUP_NAME = "TEST GROUP";

    @Autowired
    private CourseService courseService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private StudentService studentService;

    @Test
    void shouldContainsCourseWhenAddedGroupToCourse() {
        Group group = new Group(GROUP_NAME);
        groupService.save(group);
        assertThat(groupService.findAll(), hasItem(group));

        Course course = new Course(COURSE_NAME);
        courseService.save(course);

        groupService.addToCourse(group, course);
        assertThat(groupService.findCourses(group), hasItem(course));
    }

    @Test
    void shouldFindGroupWhenSaveListOfGroups() {
        Group group = new Group(GROUP_NAME);
        groupService.save(Collections.singletonList(group));
        assertThat(groupService.findAll(), hasItem(group));
    }

    @Test
    void shouldThrowExceptionWhenGetByIdAfterDeletingGroup() {
        Group group = new Group(GROUP_NAME);
        groupService.save(group);
        assertThat(groupService.findAll(), hasItem(group));

        long groupId = group.getId();
        groupService.deleteById(groupId);

        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class,
                () -> groupService.findById(groupId));
        assertEquals("ua.com.foxminded.university.data.model.Group not found by id: 1",
                ex.getMessage());
    }

    @Test
    void shouldContainsStudentWhenFindStudentAfterAddingToGroup() {
        Group group = new Group(GROUP_NAME);
        groupService.save(group);
        assertThat(groupService.findAll(), hasItem(group));

        Student student = new Student(STUDENT_FIRST_NAME, STUDENT_LAST_NAME);
        student.setGroup(group);
        studentService.save(student);

        assertThat(studentService.findAll(), hasItem(student));
        assertThat(groupService.findStudents(group), hasItem(student));
    }

    @Test
    void shouldNotContainCourseWhenGetCourseAfterDeleting() {
        Group group = new Group(GROUP_NAME);
        groupService.save(group);

        Course course = new Course(COURSE_NAME);
        courseService.save(course);

        groupService.addToCourse(group, course);
        assertThat(groupService.findCourses(group), hasItem(course));

        groupService.removeFromCourse(group, course);
        assertThat(groupService.findCourses(group), not(hasItem(course)));
    }

    @Test
    void shouldContainsCourseWhenAddListOfCoursesToGroup() {
        Group group = new Group(GROUP_NAME);
        groupService.save(group);

        Course course = new Course(COURSE_NAME);
        courseService.save(course);

        groupService.addToCourses(group, Collections.singleton(course));
        assertThat(groupService.findCourses(group), hasItem(course));
    }

}
