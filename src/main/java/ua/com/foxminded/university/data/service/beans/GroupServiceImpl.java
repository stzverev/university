package ua.com.foxminded.university.data.service.beans;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

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
        return groupDao.getById(id);
    }

    @Override
    public void addToCourses(Group group) {
        groupDao.addToCourses(group);
    }

    @Override
    public void removeFromCourse(Group group, Course course) {
        groupDao.deleteFromCourse(group, course);
    }

    @Override
    public List<TabletimeRow> getTabletime(Group group, LocalDateTime begin,
            LocalDateTime end) {
        return groupDao.getTabletime(group, begin, end);
    }

    @Override
    public void addTabletimeRows(List<TabletimeRow> tabletimeRows) {
        groupDao.addTabletimeRows(tabletimeRows);
    }

}
