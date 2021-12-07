package ua.com.foxminded.university.data.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundById;

@Service
public class GroupServiceImpl implements GroupService {

    private static final Class<Group> ENTITY_CLASS = Group.class;
    private GroupDao groupDao;

    @Autowired
    public GroupServiceImpl(GroupDao groupDao) {
        super();
        this.groupDao = groupDao;
    }

    @Override
    public void save(Group group) {
        groupDao.save(group);
    }

    @Override
    public void save(List<Group> groups) {
        groupDao.save(groups);
    }

    @Override
    public List<Group> getAll() {
        return groupDao.getAll();
    }

    @Override
    public Group getById(long id) {
        return groupDao.getById(id).orElseThrow(() -> new ObjectNotFoundById(id, ENTITY_CLASS));
    }

    @Override
    public void addToCourses(Group group, Set<Course> courses) {
        groupDao.addToCourses(group, courses);
    }

    @Override
    public void removeFromCourse(Group group, Course course) {
        groupDao.deleteFromCourse(group, course);
    }

    @Override
    public Set<TabletimeRow> getTabletime(Group group, LocalDateTime begin, LocalDateTime end) {
        return groupDao.getTabletime(group, begin, end);
    }

    @Override
    public void addTabletimeRows(List<TabletimeRow> tabletimeRows) {
        groupDao.addTabletimeRows(tabletimeRows);
    }

    @Override
    public void update(Group group) {
        groupDao.update(group);
    }

    @Override
    public Set<Student> getStudents(Group group) {
        return groupDao.getStudents(group);
    }

    @Override
    public void delete(long id) {
        groupDao.delete(id);
    }

    @Override
    public Set<Course> getCourses(Group group) {
        return groupDao.getCourses(group);
    }

}
