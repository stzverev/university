package ua.com.foxminded.university.data.service.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundById;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Class<Course> ENTITY_CLASS = Course.class;
    private CourseDao courseDao;
    private TeacherDao teacherDao;
    private GroupDao groupDao;

    @Autowired
    public CourseServiceImpl(CourseDao courseDao, TeacherDao teacherDao, GroupDao groupDao) {
        super();
        this.courseDao = courseDao;
        this.teacherDao = teacherDao;
        this.groupDao = groupDao;
    }

    @Override
    public void save(Course course) {
        courseDao.save(course);
    }

    @Override
    public void save(List<Course> courses) {
        courseDao.saveAll(courses);
    }

    @Override
    public List<Course> findAll() {
        return courseDao.findAll();
    }

    @Override
    public Course findById(long id) {
        return courseDao.findById(id)
                .orElseThrow(() -> new ObjectNotFoundById(id, ENTITY_CLASS));
    }

    @Override
    public Set<Teacher> getTeachers(Course course) {
        return courseDao.findFetchTeachersById(course.getId())
                .orElseThrow(() -> new ObjectNotFoundById(course.getId(), ENTITY_CLASS))
                .getTeachers();
    }

    @Override
    public Set<Group> getGroups(Course course) {
        return courseDao.findFetchGroupsById(course.getId())
                .orElseThrow(() -> new ObjectNotFoundById(course.getId(), ENTITY_CLASS))
                .getGroups();
    }

    @Override
    public void addGroup(Course course, Group group) {
        course = courseDao.getById(course.getId());
        group = groupDao.getById(group.getId());
        course.getGroups().add(group);
    }

    @Override
    public void removeGroupFromCourse(Course course, Group group) {
        course = courseDao.getById(course.getId());
        group = groupDao.getById(group.getId());
        course.getGroups().remove(group);
    }

    @Override
    public void addTeacher(Course course, Teacher teacher) {
        course = courseDao.getById(course.getId());
        teacher = teacherDao.getById(teacher.getId());
        course.getTeachers().add(teacher);
    }

    @Override
    public void removeTeacherFromCourse(Course course, Teacher teacher) {
        course = courseDao.getById(course.getId());
        teacher = teacherDao.getById(teacher.getId());
        course.getTeachers().remove(teacher);
    }

    @Override
    public void deleteById(long id) {
        courseDao.deleteById(id);
    }

}
