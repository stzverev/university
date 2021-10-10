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

class GroupDaoJdbcTest {

    private AnnotationConfigApplicationContext context;
    private GroupDaoJdbc groupDao;

    @BeforeEach
    private void init() {
        context = new AnnotationConfigApplicationContext(Config.class);
        DataInitializer dataInitializer =
                context.getBean(DataInitializer.class);
        dataInitializer.loadData();
        groupDao = context.getBean(GroupDaoJdbc.class);
        context.close();
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

}
