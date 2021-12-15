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
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundException;

@SpringJUnitConfig(ConfigTest.class)
@Sql(scripts = "classpath:data.sql")
class TeacherServiceImplTest {

    private static final String COURSE_NAME = "TEST COURSE";
    private static final String TEACHER_LAST_NAME = "TEACHER";
    private static final String TEACHER_FIRST_NAME = "TEST";

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @Test
    void shouldFindCourseWhenAddCourseForTeacher() {
        Teacher teacher = new Teacher(TEACHER_FIRST_NAME, TEACHER_LAST_NAME);
        teacherService.save(teacher);
        assertThat(teacherService.findAll(), hasItem(teacher));

        Course course = new Course(COURSE_NAME);
        courseService.save(course);

        teacherService.addToCourse(teacher, course);
        assertThat(teacherService.getCourses(teacher), hasItem(course));
    }

    @Test
    void shouldFindCourseWhenAddListOfCoursesForTeacher() {
        Teacher teacher = new Teacher(TEACHER_FIRST_NAME, TEACHER_LAST_NAME);
        teacherService.save(Collections.singletonList(teacher));
        assertThat(teacherService.findAll(), hasItem(teacher));

        Course course = new Course(COURSE_NAME);
        courseService.save(course);

        teacherService.addCourses(teacher, Collections.singleton(course));
        assertThat(teacherService.getCourses(teacher), hasItem(course));
    }

    @Test
    void shouldNotContainCourseWhenGetCourseAfterDeleting() {
        Teacher teacher = new Teacher(TEACHER_FIRST_NAME, TEACHER_LAST_NAME);
        teacherService.save(teacher);
        assertThat(teacherService.findAll(), hasItem(teacher));

        Course course = new Course(COURSE_NAME);
        courseService.save(course);

        teacherService.addToCourse(teacher, course);
        assertThat(teacherService.getCourses(teacher), hasItem(course));

        teacherService.removeCourse(teacher, course);
        assertThat(teacherService.getCourses(teacher), not(hasItem(course)));
    }

    @Test
    void shouldThrowExceptionWhenFindByIdAfterDeleting() {
        Teacher teacher = new Teacher(TEACHER_FIRST_NAME, TEACHER_LAST_NAME);
        teacherService.save(teacher);
        assertEquals(teacher, teacherService.findById(teacher.getId()));

        long teacherId = teacher.getId();
        teacherService.deleteById(teacherId);
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class,
                () -> teacherService.findById(teacherId));
        assertEquals("ua.com.foxminded.university.data.model.Teacher not found", ex.getMessage());
    }

}
