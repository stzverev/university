package ua.com.foxminded.university.data.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.data.db.dao.StudentDao;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.DataInitializer;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    private static final int STUDENT_TEST_ID = 1;

    @Mock
    private StudentDao studentDao;

    @InjectMocks
    private StudentServiceImpl studentService;

    @InjectMocks
    private  DataInitializer dataInitializer;

    @Test
    void shouldSaveStudentWhenSave() {
        Student student = new Student();
        studentService.save(student);
        verify(studentDao).save(student);
    }

    @Test
    void shouldGetStudentsWhenGet() {
        studentService.getAll();
        verify(studentDao).getAll();
    }

    @Test
    void shouldGetByFullNameWhenGetByName() {
        studentService.getByFullName("", "");
        verify(studentDao).getByFullName("", "");
    }

    @Test
    void shouldGetStudentByIdWhenGetById() {
        when(studentDao.getById(STUDENT_TEST_ID)).thenReturn(Optional.of(new Student()));
        studentService.getById(STUDENT_TEST_ID);
        verify(studentDao).getById(STUDENT_TEST_ID);
    }

    @Test
    void shouldSaveStudentsWhenSaveListStudents() {
        List<Student> students = new ArrayList<>();
        studentService.save(students);
        verify(studentDao).save(students);
    }

}
