package ua.com.foxminded.university.data.db.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.DataInitializer;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;

class StudentDaoJdbcTest {

    private AnnotationConfigApplicationContext context;
    private StudentDaoJdbc studentDao;

    @BeforeEach
    private void init() {
        context = new AnnotationConfigApplicationContext(Config.class);
        DataInitializer dataInitializer =
                context.getBean(DataInitializer.class);
        dataInitializer.loadData();
        studentDao = context.getBean(StudentDaoJdbc.class);
        context.close();
    }

    @Test
    void shouldGetByNameStudentWhenSaveStudent() {
        String firstName = "Albert";
        String lastName = "Einstein";
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setGroup(new Group());
        studentDao.save(student);
        Student expected = student;

        Student actual = studentDao.getByFullName(firstName, lastName);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetListStudentsWhenSaveList() {
        List<Student> students = new ArrayList<>();
        Student student1 = new Student();
        student1.setFirstName("Albert");
        student1.setLastName("Einstein");
        student1.setGroup(new Group());
        students.add(student1);
        Student student2 = new Student();
        student2.setFirstName("Francis");
        student2.setLastName("Bacon");
        student2.setGroup(new Group());
        students.add(student2);
        studentDao.save(students);

        List<Student> actual = studentDao.getAll();

        assertEquals(students, actual);
    }

    @Test
    void shouldGetStudentByIdWhenSaveStudent() {
        String firstName = "Blaize";
        String lastName = "Pascal";
        Student expected = new Student();
        expected.setFirstName(firstName);
        expected.setLastName(lastName);
        expected.setGroup(new Group());
        studentDao.save(expected);
        expected = studentDao.getByFullName(firstName, lastName);

        Student actual = studentDao.getById(expected.getId());

        assertEquals(expected, actual);
    }

}
