package ua.com.foxminded.university.data.service.beans;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.service.GroupService;

@Service
public class GroupServiceImpl implements GroupService {

    private GroupDao groupDao;
    private final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Autowired
    public GroupServiceImpl(GroupDao groupDao) {
        super();
        this.groupDao = groupDao;
    }

    @Override
    public void save(Group group) {
        logger.debug("The savving of group: {}", group);
        try {
            groupDao.save(group);
        } catch (Exception e) {
            logger.error("Error when saving the {}: {}",
                    group, e.getMessage());
            throw e;
        }
    }

    @Override
    public void save(List<Group> groups) {
        logger.debug("The saving of list groups: list length{}", groups.size());
        try {
            groupDao.save(groups);
        } catch (Exception e) {
            logger.error(""
                    + "Error when saving the list of groups:%n"
                    + "groups count: {}%n"
                    + "error: {}", groups.size(), e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Group> getAll() {
        logger.debug("The getting of all groups");
        return groupDao.getAll();
    }

    @Override
    public Group getById(long id) {
        logger.debug("The getting of group by id: id: {}", id);
        return groupDao.getById(id);
    }

    @Override
    public void addToCourses(Group group) {
        logger.debug("The adding of group {} to the {} courses has started",
                group, group.getCourses().size());
        try {
            groupDao.addToCourses(group);
        } catch (Exception e) {
            logger.error(""
                    + "Error when adding courses to group:%n"
                    + "group: {}%n"
                    + "courses count: {}",
                    group.getName(), group.getCourses().size());
            throw e;
        }
    }

    @Override
    public void removeFromCourse(Group group, Course course) {
        logger.debug("The removing of group {} from course {} has started",
                group, course);
        try {
            groupDao.deleteFromCourse(group, course);
        } catch (Exception e) {
            logger.error(""
                    + "Error when removing course from group:%n"
                    + "group: {}%n"
                    + "course: {}", group, course);
            throw e;
        }
    }

    @Override
    public List<TabletimeRow> getTabletime(Group group, LocalDateTime begin,
            LocalDateTime end) {
        logger.debug(""
                + "The getting of tabletime has started%n"
                + "group: {}; begin: {}; end: {}",
                group, begin, end);
        return groupDao.getTabletime(group, begin, end);
    }

    @Override
    public void addTabletimeRows(List<TabletimeRow> tabletimeRows) {
        logger.debug("The adding of tabletime rows has started:%n rows count - {}",
                tabletimeRows.size());
        try {
            groupDao.addTabletimeRows(tabletimeRows);
        } catch (Exception e) {
            logger.error(""
                     + "Error when adding tabletime rows: rows count - {};%n error: {}",
                     tabletimeRows.size(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void update(Group group) {
        groupDao.update(group);
    }

    @Override
    public List<Student> getStudents(Group group) {
        return groupDao.getStudents(group);
    }

    @Override
    public void delete(long id) {
        groupDao.delete(id);
    }

    @Override
    public List<Course> getCourses(Group group) {
        return groupDao.getCourses(group);
    }

}
