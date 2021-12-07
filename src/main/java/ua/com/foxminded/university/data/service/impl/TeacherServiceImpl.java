package ua.com.foxminded.university.data.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundById;

@Service
public class TeacherServiceImpl implements TeacherService {

    private static final Class<Teacher> ENTITY_CLASS = Teacher.class;
    private TeacherDao teacherDao;

    @Autowired
    public TeacherServiceImpl(TeacherDao teacherDao) {
        super();
        this.teacherDao = teacherDao;
    }

    @Override
    public void save(Teacher teacher) {
        teacherDao.save(teacher);
    }

    @Override
    public void save(List<Teacher> teachers) {
        teacherDao.save(teachers);
    }

    @Override
    public List<Teacher> getAll() {
        return teacherDao.getAll();
    }

    @Override
    public Teacher getById(long id) {
        return teacherDao.getById(id).orElseThrow(() -> new ObjectNotFoundById(id, ENTITY_CLASS));
    }

    @Override
    public void addCourses(Teacher teacher, Set<Course> courses) {
        teacherDao.addToCourses(teacher, courses);
    }

    @Override
    public void removeCourse(Teacher teacher, Course course) {
        teacherDao.removeCourse(teacher, course);
    }

    @Override
    public Set<TabletimeRow> getTabletime(Teacher teacher, LocalDateTime begin,
            LocalDateTime end) {
        return teacherDao.getTabletime(teacher, begin, end);
    }

    @Override
    public void addTabletimeRows(List<TabletimeRow> tabletimeRows) {
        teacherDao.addTabletimeRows(tabletimeRows);
    }

    @Override
    public void update(Teacher teacher) {
        teacherDao.update(teacher);
    }

    @Override
    public Set<Course> getCourses(Teacher teacher) {
        return teacherDao.getCourses(teacher);
    }

    @Override
    public void delete(long id) {
        teacherDao.delete(id);

    }

    @Override
    public void removeFromCourse(Teacher teacher, Course course) {
        teacherDao.removeCourse(teacher, course);
    }

    @Override
    public void addToCourse(Teacher teacher, Course course) {
        teacherDao.addToCourses(teacher, Collections.singleton(course));
    }

}
