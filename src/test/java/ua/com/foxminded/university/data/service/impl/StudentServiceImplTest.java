package ua.com.foxminded.university.data.service.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.ConfigTest;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.StudentService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundException;

@SpringJUnitConfig(ConfigTest.class)
@Sql(scripts = "classpath:data.sql")
class StudentServiceImplTest {

    private static final String STUDENT_FIRST_NAME = "Sheldon";
    private static final String STUDENT_LAST_NAME = "Cooper";

    @Autowired
    private StudentService studentService;

    @Test
    void shouldGetStudentByFullNameWhenSaveStudent() {
        Student expected = new Student(STUDENT_FIRST_NAME, STUDENT_LAST_NAME);
        studentService.save(expected);

        Student actual = studentService
                .getByFullName(STUDENT_FIRST_NAME, STUDENT_LAST_NAME);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetStudentByIdWhenSaveListOfStudents() {
        Student expected = new Student(STUDENT_FIRST_NAME, STUDENT_LAST_NAME);
        studentService.save(Collections.singletonList(expected));
        assertNotEquals(0, expected.getId());

        Student actual = studentService.findById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotContainStudentWhenGetStudentAfterDeleting() {
        Student student = new Student(STUDENT_FIRST_NAME, STUDENT_LAST_NAME);
        studentService.save(student);
        assertThat(studentService.findAll(), hasItem(student));

        studentService.deleteById(student.getId());

        assertThat(studentService.findAll(), not(hasItem(student)));
    }

    @Test
    void shouldThrowExceptionWhenNotFoundByFullName() {
        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class,
                () -> studentService.getByFullName(STUDENT_FIRST_NAME, STUDENT_LAST_NAME));
        assertEquals("ua.com.foxminded.university.data.model.Student not found", ex.getMessage());
    }

}
