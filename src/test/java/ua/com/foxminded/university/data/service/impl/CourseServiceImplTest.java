package ua.com.foxminded.university.data.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Teacher;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    private static final int COURSE_TEST_ID = 1;

    @Mock
    private CourseDao courseDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private TeacherDao teacherDao;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void shouldAddToCoursesWhenAddGroupToCourse() {
        Group group = new Group();
        Course course = new Course();
        courseService.addGroup(course, group);
        verify(groupDao).addToCourses(Mockito.eq(group), Mockito.eq(Collections.singleton(course)));
    }

    @Test
    void shouldAddToCoursesWhenAddTeacherToCourse() {
        Teacher teacher = new Teacher();
        courseService.addTeacher(new Course(), teacher);
        verify(teacherDao).addToCourses(Mockito.eq(teacher), Mockito.any());
    }

    @Test
    void shouldSaveCourseWhenSave() {
        Course course = new Course();
        courseService.save(course);
        verify(courseDao).save(course);
    }

    @Test
    void shouldGetCoursesWhenGet() {
        courseService.getAll();
        verify(courseDao).getAll();
    }

    @Test
    void shouldGetCourseByIdWhenGetById() {
        when(courseDao.getById(COURSE_TEST_ID)).thenReturn(Optional.of(new Course()));
        courseService.getById(COURSE_TEST_ID);
        verify(courseDao).getById(COURSE_TEST_ID);
    }

    @Test
    void shouldSaveCoursesWhenSaveListCourses() {
        List<Course> courses = new ArrayList<>();
        courseService.save(courses);
        verify(courseDao).save(courses);
    }

    @Test
    void shouldGetGroupWhenGetGroup() {
        Course course = new Course();
        courseService.getGroups(course);
        verify(courseDao).getGroups(course);
    }

    @Test
    void shouldGetTeachersWhenGetTeacher() {
        Course course = new Course();
        courseService.getTeachers(course);
        verify(courseDao).getTeachers(course);
    }

    @Test
    void shouldRemoveGroupWhenRemoveGroup() {
        Course course = new Course();
        Group group = new Group();
        courseService.removeGroup(course, group);
        verify(groupDao).deleteFromCourse(group, course);
    }

    @Test
    void shouldRemoveTeacherWhenRemoveTeacher() {
        Course course = new Course();
        Teacher teacher = new Teacher();
        courseService.removeTeacherFromCourse(course, teacher);
        verify(teacherDao).removeCourse(teacher, course);
    }

}
