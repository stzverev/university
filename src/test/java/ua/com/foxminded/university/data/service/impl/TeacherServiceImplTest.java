package ua.com.foxminded.university.data.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {

    private static final int TEACHER_ID_TEST = 1;

    @Mock
    private TeacherDao teacherDao;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Test
    void shouldSaveTeacherWhenSave() {
        Teacher teacher = new Teacher();
        teacherService.save(teacher);
        verify(teacherDao).save(teacher);
    }

    @Test
    void shouldGetTeachersWhenGet() {
        teacherService.getAll();
        verify(teacherDao).getAll();
    }

    @Test
    void shouldGetTeacherByIdWhenGetById() {
        when(teacherDao.getById(TEACHER_ID_TEST)).thenReturn(Optional.of(new Teacher()));
        teacherService.getById(TEACHER_ID_TEST);
        verify(teacherDao).getById(TEACHER_ID_TEST);
    }

    @Test
    void shouldSaveTeachersWhenSaveListTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teacherService.save(teachers);
        verify(teacherDao).save(teachers);
    }

    @Test
    void shouldAddTabletimeRowsWhenAddTabletime() {
        Set<TabletimeRow> rows = new HashSet<>();
        teacherService.addTabletimeRows(rows);
        verify(teacherDao).addTabletimeRows(rows);
    }

    @Test
    void shouldAddCoursesWhenAddCourses() {
        Teacher teacher = new Teacher();
        Set<Course> courses = new HashSet<>();
        teacherService.addCourses(teacher, courses);
        verify(teacherDao).addToCourses(teacher, courses);
    }

    @Test
    void shouldGetTabletimeWhenGetTabletime() {
        Teacher teacher = new Teacher();
        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        teacherService.getTabletime(teacher, begin, end);
        verify(teacherDao).getTabletime(teacher, begin, end);
    }

    @Test
    void shouldRemoveFromCourseWhenRemove() {
        Teacher teacher = new Teacher();
        Course course = new Course();
        teacherService.removeCourse(teacher, course);
        verify(teacherDao).removeCourse(teacher, course);
    }

}
