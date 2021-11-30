package ua.com.foxminded.university.data.service.impl;

import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
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
import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.DataInitializer;
import ua.com.foxminded.university.data.service.impl.TeacherServiceImpl;

@SpringJUnitConfig(ConfigTest.class)
@ExtendWith(SpringExtension.class)
class TeacherServiceImplTest {

    @MockBean
    private TeacherDao teacherDao;

    @Autowired
    private TeacherServiceImpl teacherService;

    @Autowired
    private  DataInitializer dataInitializer;

    @BeforeEach
    private void init() {
        dataInitializer.loadData();
    }

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
        teacherService.getById(1);
        verify(teacherDao).getById(1);
    }

    @Test
    void shouldSaveTeachersWhenSaveListTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teacherService.save(teachers);
        verify(teacherDao).save(teachers);
    }

    @Test
    void shouldAddTabletimeRowsWhenAddTabletime() {
        List<TabletimeRow> rows = new ArrayList<>();
        teacherService.addTabletimeRows(rows);
        verify(teacherDao).addTabletimeRows(rows);
    }

    @Test
    void shouldAddCoursesWhenAddCourses() {
        Teacher teacher = new Teacher();
        teacher.setCourses(new ArrayList<Course>());
        teacherService.addCourses(teacher);
        verify(teacherDao).addToCourses(teacher);
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
