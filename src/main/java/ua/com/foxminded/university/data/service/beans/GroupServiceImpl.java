package ua.com.foxminded.university.data.service.beans;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(
            GroupServiceImpl.class);

    @Autowired
    public GroupServiceImpl(GroupDao groupDao) {
        super();
        this.groupDao = groupDao;
    }

    @Override
    public void save(Group group) {
        try {
            groupDao.save(group);
        } catch (Exception e) {
            logger.error("Error when saving the {}: {}",
                    group.getName(),
                    e.getMessage());
        }
    }

    @Override
    public void save(List<Group> groups) {
        try {
            groupDao.save(groups);
        } catch (Exception e) {
            logger.error("Error when saving the list of groups: {}",
                    e.getMessage());
        }
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
        try {
            groupDao.addToCourses(group);
        } catch (Exception e) {
            String coursesName = group.getCourses()
                    .stream()
                    .map(Course::getName)
                    .collect(Collectors.joining(", "));
            logger.error(""
                    + "Error when adding courses to group:%n"
                    + "group: {}%n"
                    + "courses: {}",
                    group.getName(),
                    coursesName);
        }
    }

    @Override
    public void removeFromCourse(Group group, Course course) {
        try {
            groupDao.deleteFromCourse(group, course);
        } catch (Exception e) {
            logger.error(""
                    + "Error when removing course from group:%n"
                    + "group: {}%n"
                    + "course: {}",
                    group.getName(),
                    course.getName());
        }
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
