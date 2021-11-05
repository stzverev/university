package ua.com.foxminded.university.data.service.beans;

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

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.DataInitializer;
import ua.com.foxminded.university.data.db.dao.StudentDao;
import ua.com.foxminded.university.data.model.Student;

@SpringJUnitConfig(Config.class)
@ExtendWith(SpringExtension.class)
class StudentServiceImplTest {

    @MockBean
    private StudentDao studentDao;

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private  DataInitializer dataInitializer;

    @BeforeEach
    private void init() {
        dataInitializer.loadData();
    }

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
        studentService.getById(1);
        verify(studentDao).getById(1);
    }

    @Test
    void shouldSaveStudentsWhenSaveListStudents() {
        List<Student> students = new ArrayList<>();
        studentService.save(students);
        verify(studentDao).save(students);
    }

}
