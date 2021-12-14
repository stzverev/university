package ua.com.foxminded.university.data.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundById;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private static final Class<Group> ENTITY_CLASS = Group.class;
    private GroupDao groupDao;
    private CourseDao courseDao;

    @Autowired
    public GroupServiceImpl(GroupDao groupDao, CourseDao courseDao) {
        super();
        this.groupDao = groupDao;
        this.courseDao = courseDao;
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
    public List<Group> findAll() {
        return groupDao.findAll();
    }

    @Override
    public Group findById(long id) {
        return groupDao.findById(id)
                .orElseThrow(() -> new ObjectNotFoundById(id, ENTITY_CLASS));
    }

    @Override
    public void addToCourses(Group group, Set<Course> courses) {
        long id = group.getId();
        group = groupDao.findById(id)
                .orElseThrow(() -> new ObjectNotFoundById(id, ENTITY_CLASS));
        Set<Long> coursesId = courses.stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        courseDao.findAllById(coursesId)
            .stream()
            .forEach(group.getCourses()::add);
    }

    @Override
    public void removeFromCourse(Group group, Course course) {
        long id = group.getId();
        group = groupDao.findById(id)
                .orElseThrow(() -> new ObjectNotFoundById(id, ENTITY_CLASS));
        course = courseDao.getById(course.getId());
        group.getCourses().remove(course);
    }

    @Override
    public Set<Student> getStudents(Group group) {
        return groupDao.findFetchStudentsById(group.getId())
                .orElseThrow(() -> new ObjectNotFoundById(group.getId(), ENTITY_CLASS))
                .getStudents();
    }

    @Override
    public void deleteById(long id) {
        groupDao.deleteById(id);
    }

    @Override
    public Set<Course> getCourses(Group group) {
        return groupDao.findFetchCoursesById(group.getId())
                .orElseThrow(() -> new ObjectNotFoundById(group.getId(), ENTITY_CLASS))
                .getCourses();
    }

    @Override
    public void addToCourse(Group group, Course course) {
        long id = group.getId();
        group = groupDao.findById(id)
                .orElseThrow(() -> new ObjectNotFoundById(id, ENTITY_CLASS));
        long courseId = course.getId();
        course = courseDao.findById(courseId)
                .orElseThrow(() -> new ObjectNotFoundById(courseId, Course.class));
        group.getCourses().add(course);
    }

}
