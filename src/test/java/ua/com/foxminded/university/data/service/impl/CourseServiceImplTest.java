package ua.com.foxminded.university.data.service.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundException;

@SpringBootTest
class CourseServiceImplTest {

    private static final String TEACHER_LAST_NAME = "Teacher";
    private static final String TEACHER_FIRST_NAME = "Test";
    private static final String COURSE_NAME = "Test course";
    private static final String GROUP_NAME = "Test group";

    @Autowired
    private CourseService courseService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private TeacherService teacherService;

    @Test
    void shouldContainsGroupWhenAddedGroup() {
        Group group = new Group(GROUP_NAME);
        Course course = new Course(COURSE_NAME);
        groupService.save(group);
        courseService.save(course);
        courseService.addGroup(course, group);

        Set<Group> actual = courseService.getGroups(course);

        assertThat(actual, hasItem(group));
    }

    @Test
    void shouldContainsTeacherWhenAddedTeacher() {
        Teacher teacher = new Teacher(TEACHER_FIRST_NAME, TEACHER_LAST_NAME);
        Course course = new Course(COURSE_NAME);
        teacherService.save(teacher);
        courseService.save(course);
        courseService.addTeacher(course, teacher);

        Set<Teacher> actual = courseService.getTeachers(course);

        assertThat(actual, hasItem(teacher));
    }

    @Test
    void shouldContainsCourseWhenFindAllCoursesAfterCreatingNew() {
        Course course = new Course(COURSE_NAME);
        courseService.save(course);

        List<Course> actual = courseService.findAll();

        assertThat(actual, hasItem(course));
    }

    @Test
    void shouldContainsCourseWhenFindAllCoursesAfterSavingListCourses() {
        Course course = new Course(COURSE_NAME);
        courseService.save(Collections.singletonList(course));

        List<Course> actual = courseService.findAll();

        assertThat(actual, hasItem(course));
    }

    @Test
    void shouldThrowExceptionWhenFindCourseAfterCourseHasDeleted() {
        Course course = new Course(COURSE_NAME);
        courseService.save(course);
        long courseId = course.getId();
        assertEquals(course, courseService.findById(courseId));

        courseService.deleteById(courseId);
        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class,
                () -> courseService.findById(courseId));
        String message = "ua.com.foxminded.university.data.model.Course not found by id: " + courseId;
        assertEquals(message, e.getMessage());
    }

    @Test
    void shouldNotContainTeacherAfterDeletingTeacherFromCourse() {
        Course course = new Course(COURSE_NAME);
        courseService.save(course);
        long courseId = course.getId();
        assertEquals(course, courseService.findById(courseId));

        Teacher teacher = new Teacher(TEACHER_FIRST_NAME, TEACHER_LAST_NAME);
        teacherService.save(teacher);
        assertThat(teacherService.findAll(), hasItem(teacher));

        courseService.addTeacher(course, teacher);
        assertThat(courseService.getTeachers(course), hasItem(teacher));

        courseService.removeTeacherFromCourse(course, teacher);
        assertThat(courseService.getTeachers(course), not(hasItem(teacher)));
    }

    @Test
    void shouldNotContainGroupAfterDeletingGroupFromCourse() {
        Course course = new Course(COURSE_NAME);
        courseService.save(course);
        long courseId = course.getId();
        assertEquals(course, courseService.findById(courseId));

        Group group = new Group(GROUP_NAME);
        groupService.save(group);
        long groupId = group.getId();
        assertEquals(group, groupService.findById(groupId));

        courseService.addGroup(course, group);
        assertThat(courseService.getGroups(course), hasItem(group));

        courseService.removeGroupFromCourse(course, group);
        assertThat(courseService.getGroups(course), not(hasItem(group)));
    }

}
