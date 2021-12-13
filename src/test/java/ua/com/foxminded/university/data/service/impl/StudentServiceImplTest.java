package ua.com.foxminded.university.data.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.ConfigTest;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.StudentService;

@SpringJUnitConfig(ConfigTest.class)
@Sql(scripts = "classpath:data.sql")
class StudentServiceImplTest {

    private static final String STUDENT_FIRST_NAME = "Sheldon";

    private static final String STUDENT_LAST_NAME = "Cooper";

    @Autowired
    private StudentService studentService;

    @Test
    void shouldGetStudentByFullNameWhenSaveStudent() {
        Student expected = new Student();
        expected.setFirstName(STUDENT_FIRST_NAME);
        expected.setLastName(STUDENT_LAST_NAME);
        studentService.save(expected);

        Student actual = studentService
                .getByFullName(STUDENT_FIRST_NAME, STUDENT_LAST_NAME);

        assertEquals(expected, actual);
    }

}
