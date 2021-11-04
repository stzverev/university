package ua.com.foxminded.university.data.service.beans;

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

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.DataInitializer;
import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.TabletimeRow;

@SpringJUnitConfig(Config.class)
@ExtendWith(SpringExtension.class)
class GroupServiceImplTest {

    @MockBean
    private GroupDao groupDao;

    @Autowired
    private GroupServiceImpl groupService;

    @Autowired
    private  DataInitializer dataInitializer;

    @BeforeEach
    private void init() {
        dataInitializer.loadData();
    }

    @Test
    void shouldSaveGroupWhenSave() {
        Group group = new Group();
        groupService.save(group);
        verify(groupDao).save(group);
    }

    @Test
    void shouldGetGroupsWhenGet() {
        groupService.getAll();
        verify(groupDao).getAll();
    }

    @Test
    void shouldGetGroupByIdWhenGetById() {
        groupService.getById(1);
        verify(groupDao).getById(1);
    }

    @Test
    void shouldSaveGroupsWhenSaveListGroups() {
        List<Group> groups = new ArrayList<>();
        groupService.save(groups);
        verify(groupDao).save(groups);
    }

    @Test
    void shouldAddTabletimeRowsWhenAddTabletime() {
        List<TabletimeRow> rows = new ArrayList<>();
        groupService.addTabletimeRows(rows);
        verify(groupDao).addTabletimeRows(rows);
    }

    @Test
    void shouldAddCoursesWhenAddCourses() {
        Group group = new Group();
        group.setCourses(new ArrayList<Course>());
        groupService.addToCourses(group);
        verify(groupDao).addToCourses(group);
    }

    @Test
    void shouldGetTabletimeWhenGetTabletime() {
        Group group = new Group();
        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        groupService.getTabletime(group, begin, end);
        verify(groupDao).getTabletime(group, begin, end);
    }

    @Test
    void shouldRemoveFromCourseWhenRemove() {
        Group group = new Group();
        Course course = new Course();
        groupService.removeFromCourse(group, course);
        verify(groupDao).deleteFromCourse(group, course);
    }

}
