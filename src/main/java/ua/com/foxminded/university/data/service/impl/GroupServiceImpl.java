package ua.com.foxminded.university.data.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.db.dao.TabletimeDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.GroupService;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private GroupDao groupDao;
    private CourseDao courseDao;
    private TabletimeDao tabletimeDao;

    @Autowired
    public GroupServiceImpl(GroupDao groupDao, CourseDao courseDao, TabletimeDao tabletimeDao) {
        super();
        this.groupDao = groupDao;
        this.courseDao = courseDao;
        this.tabletimeDao = tabletimeDao;
    }

    @Override
    public void save(Group group) {
        groupDao.save(group);
    }

    @Override
    public void save(List<Group> groups) {
        groupDao.saveAll(groups);
    }

    @Override
    public List<Group> getAll() {
        return groupDao.findAll();
    }

    @Override
    public Group getById(long id) {
        return groupDao.getById(id);
    }

    @Override
    public void addToCourses(Group group, Set<Course> courses) {
        group = groupDao.getById(group.getId());
        Set<Long> coursesId = courses.stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        courseDao.findAllById(coursesId)
            .stream()
            .forEach(group.getCourses()::add);
    }

    @Override
    public void removeFromCourse(Group group, Course course) {
        group = groupDao.getById(group.getId());
        course = courseDao.getById(course.getId());
        group.getCourses().remove(course);
    }

    @Override
    public Set<Student> getStudents(Group group) {
        return groupDao.getById(group.getId()).getStudents();
    }

    @Override
    public void delete(long id) {
        groupDao.deleteById(id);
    }

    @Override
    public Set<Course> getCourses(Group group) {
        return groupDao.getById(group.getId()).getCourses();
    }

}
