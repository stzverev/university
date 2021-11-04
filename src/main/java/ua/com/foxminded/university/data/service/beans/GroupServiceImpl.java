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
        logger.debug("The savving of group: {}", group);
        try {
            groupDao.save(group);
        } catch (Exception e) {
            logger.error("Error when saving the {}: {}",
                    group, e.getMessage());
        }
    }

    @Override
    public void save(List<Group> groups) {
        String groupsDescription = groups
                .stream()
                .map(Group::toString)
                .collect(Collectors.joining("; " + System.lineSeparator()));
        logger.debug("The saving of list groups: {}", groupsDescription );
        try {
            groupDao.save(groups);
        } catch (Exception e) {
            logger.error(""
                    + "Error when saving the list of groups:%n"
                    + "groups: {}%n"
                    + "error: {}", groupsDescription, e.getMessage());
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
        String coursesName = group.getCourses()
                .stream()
                .map(Course::toString)
                .collect(Collectors.joining(", "));
        logger.debug("The adding of group {} to the courses {} has started",
                group, coursesName);
        try {
            groupDao.addToCourses(group);
        } catch (Exception e) {
            logger.error(""
                    + "Error when adding courses to group:%n"
                    + "group: {}%n"
                    + "courses: {}",
                    group.getName(), coursesName);
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
        String tabletimeRowsDescription = tabletimeRows
                .stream()
                .map(TabletimeRow::toString)
                .collect(Collectors.joining("; " + System.lineSeparator()));
        logger.debug("The adding of tabletime rows has started:%n{}",
                tabletimeRowsDescription);
        try {
            groupDao.addTabletimeRows(tabletimeRows);
        } catch (Exception e) {
            logger.error(""
                     + "Error when adding tabletime rows: {};%n error: {}",
                     tabletimeRowsDescription, e.getMessage());
        }
    }

}
