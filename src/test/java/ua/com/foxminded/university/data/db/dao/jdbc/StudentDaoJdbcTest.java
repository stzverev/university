package ua.com.foxminded.university.data.db.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.DataInitializer;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;

@SpringJUnitConfig(Config.class)
class StudentDaoJdbcTest {

    @Autowired
    private StudentDaoJdbc studentDao;

    @Autowired
    private GroupDaoJdbc groupDao;

    @Autowired
    private DataInitializer dataInitializer;

    @BeforeEach
    private void init() {
        dataInitializer.loadData();
    }

    @Test
    void shouldGetByNameStudentWhenSaveStudent() {
        Group group = saveAndGetTestGroup();
        String firstName = "Albert";
        String lastName = "Einstein";
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setGroup(group);
        studentDao.save(student);
        Student expected = student;

        Student actual = studentDao.getByFullName(firstName, lastName);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetByNameStudentWhenUpdateStudent() {
        Group group = saveAndGetTestGroup();
        String firstName = "Albert";
        String lastName = "Einstein";
        String newFirstName = "Albert2";
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setGroup(group);
        studentDao.save(student);
        student = studentDao.getByFullName(firstName, lastName);
        student.setFirstName(newFirstName);
        studentDao.update(student);
        Student expected = student;

        Student actual = studentDao.getByFullName(newFirstName, lastName);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetListStudentsWhenSaveList() {
        Group group = saveAndGetTestGroup();
        List<Student> students = new ArrayList<>();
        Student student1 = new Student();
        student1.setFirstName("Albert");
        student1.setLastName("Einstein");
        student1.setGroup(group);
        students.add(student1);
        Student student2 = new Student();
        student2.setFirstName("Francis");
        student2.setLastName("Bacon");
        student2.setGroup(group);
        students.add(student2);
        studentDao.save(students);

        List<Student> actual = studentDao.getAll();

        assertEquals(students, actual);
    }

    @Test
    void shouldGetStudentByIdWhenSaveStudent() {
        Group group = saveAndGetTestGroup();
        String firstName = "Blaize";
        String lastName = "Pascal";
        Student expected = new Student();
        expected.setFirstName(firstName);
        expected.setLastName(lastName);
        expected.setGroup(group);
        studentDao.save(expected);
        expected = studentDao.getByFullName(firstName, lastName);

        Student actual = studentDao.getById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowExceptionWhenGetStudenThatWasDeleted() {
        String firstName = "Blaize";
        String lastName = "Pascal";
        Group group = saveAndGetTestGroup();
        Student student = saveAndGetStudent(firstName, lastName, group);
        studentDao.delete(student.getId());

        long studentId = student.getId();
        Throwable throwable = assertThrows(DataAccessException.class, () -> studentDao.getById(studentId));
        assertEquals("Incorrect result size: expected 1, actual 0", throwable.getMessage());
    }

    private Student saveAndGetStudent(String firstName, String lastName, Group group) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setGroup(group);
        studentDao.save(student);
        return studentDao.getByFullName(firstName, lastName);
    }

    private Group saveAndGetTestGroup() {
        String groupName = "B4";
        Group group = new Group();
        group.setName(groupName);
        groupDao.save(group);
        return groupDao.getByName(groupName);
    }

}
