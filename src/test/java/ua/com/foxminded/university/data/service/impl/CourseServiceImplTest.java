package ua.com.foxminded.university.data.service.impl;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.ConfigTest;
import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.DataInitializer;
import ua.com.foxminded.university.data.service.impl.CourseServiceImpl;

@SpringJUnitConfig(ConfigTest.class)
@ExtendWith(SpringExtension.class)
class CourseServiceImplTest {

    @MockBean
    private CourseDao courseDao;

    @MockBean
    private GroupDao groupDao;

    @MockBean
    private TeacherDao teacherDao;

    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    private  DataInitializer dataInitializer;

    @BeforeEach
    private void init() {
        dataInitializer.loadData();
    }

    @Test
    void shouldAddToCoursesWhenAddGroupToCourse() {
        Group group = new Group();
        courseService.addGroup(new Course(), group);
        verify(groupDao).addToCourses(group);
    }

    @Test
    void shouldAddToCoursesWhenAddTeacherToCourse() {
        Teacher teacher = new Teacher();
        courseService.addTeacher(new Course(), teacher);
        verify(teacherDao).addToCourses(teacher);
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
        courseService.getById(1);
        verify(courseDao).getById(1);
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
