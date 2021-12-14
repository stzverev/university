package ua.com.foxminded.university.data.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.db.dao.TabletimeDao;
import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.TeacherService;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private TeacherDao teacherDao;
    private CourseDao courseDao;
    private TabletimeDao tabletimeDao;

    @Autowired
    public TeacherServiceImpl(TeacherDao teacherDao, CourseDao courseDao) {
        super();
        this.teacherDao = teacherDao;
        this.courseDao = courseDao;
    }

    @Override
    public void save(Teacher teacher) {
        teacherDao.save(teacher);
    }

    @Override
    public void save(List<Teacher> teachers) {
        teacherDao.saveAll(teachers);
    }

    @Override
    public List<Teacher> findAll() {
        return teacherDao.findAll();
    }

    @Override
    public Teacher findById(long id) {
        return teacherDao.getById(id);
    }

    @Override
    public void addCourses(Teacher teacher, Set<Course> courses) {
        teacher = teacherDao.getById(teacher.getId());
        Set<Long> coursesId = courses.stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        List<Course> foundCourses = courseDao.findAllById(coursesId);
        foundCourses.stream().forEach(teacher.getCourses()::add);
    }

    @Override
    public void removeCourse(Teacher teacher, Course course) {
        teacher = teacherDao.getById(teacher.getId());
        course = courseDao.getById(course.getId());
        teacher.getCourses().remove(course);
    }

    @Override
    public Set<Course> getCourses(Teacher teacher) {
        return teacherDao.getById(teacher.getId()).getCourses();
    }

    @Override
    public void deleteById(long id) {
        teacherDao.deleteById(id);
    }

    @Override
    public void addToCourse(Teacher teacher, Course course) {
        teacher = teacherDao.getById(teacher.getId());
        course = courseDao.getById(course.getId());
        teacher.getCourses().add(course);
    }

}
