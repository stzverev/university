package ua.com.foxminded.university.data.service.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.TabletimeRow;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    private static final long GROUP_ID_TEST = 1;

    @Mock
    private GroupDao groupDao;

    @InjectMocks
    private GroupServiceImpl groupService;

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
        when(groupDao.getById(GROUP_ID_TEST)).thenReturn(Optional.of(new Group()));
        groupService.getById(GROUP_ID_TEST);
        verify(groupDao).getById(GROUP_ID_TEST);
    }

    @Test
    void shouldSaveGroupsWhenSaveListGroups() {
        List<Group> groups = new ArrayList<>();
        groupService.save(groups);
        verify(groupDao).save(groups);
    }

    @Test
    void shouldAddTabletimeRowsWhenAddTabletime() {
        Set<TabletimeRow> rows = new HashSet<>();
        groupService.addTabletimeRows(rows);
        verify(groupDao).addTabletimeRows(rows);
    }

    @Test
    void shouldAddCoursesWhenAddCourses() {
        Group group = new Group();
        Set<Course> courses = new HashSet<>();
        groupService.addToCourses(group, courses);
        verify(groupDao).addToCourses(group, courses);
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
