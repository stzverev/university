package ua.com.foxminded.university.data.db.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.DataInitializer;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;

@SpringJUnitConfig(Config.class)
class GroupDaoJdbcTest {

    @Autowired
    private GroupDaoJdbc groupDao;

    @Autowired
    private StudentDaoJdbc studentDao;

    @Autowired
    private DataInitializer dataInitializer;

    @BeforeEach
    private void init() {
        dataInitializer.loadData();
    }

    @Test
    void shouldGetByNameGroupWhenSaveGroup() {
        String name = "MI6";
        Group group = new Group();
        group.setName(name);
        groupDao.save(group);
        Group expected = group;

        Group actual = groupDao.getByName(name);

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetListGroupsWhenSaveList() {
        List<Group> groups = new ArrayList<>();
        Group group1 = new Group();
        group1.setName("MI6");
        groups.add(group1);
        Group group2 = new Group();
        group2.setName("KGB");
        groups.add(group2);
        groupDao.save(groups);

        List<Group> actual = groupDao.getAll();

        assertEquals(groups, actual);
    }

    @Test
    void shouldGetGroupByIdWhenSaveGroup() {
        String name = "CIA";
        Group expected = new Group();
        expected.setName(name);
        groupDao.save(expected);
        expected = groupDao.getByName(name);

        Group actual = groupDao.getById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    void shouldGetStudentsWhenSaveStudentToGroupThenOk() {
        String groupName = "Elementary physics";
        Group group = new Group();
        group.setName(groupName);
        groupDao.save(group);
        group = groupDao.getByName(groupName);
        Student student = new Student();
        student.setFirstName("Albert");
        student.setLastName("Einstein");
        student.setGroup(group);
        studentDao.save(student);
        List<Student> expected = Collections.singletonList(student);

        List<Student> actual = groupDao.getStudents(group);

        assertEquals(expected, actual);
    }

}
