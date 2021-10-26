package ua.com.foxminded.university.data.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Teacher;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseDao courseDao;
    private TeacherDao teacherDao;
    private GroupDao groupDao;

    @Autowired
    public CourseServiceImpl(CourseDao courseDao,
            TeacherDao teacherDao, GroupDao groupDao) {
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
        courseDao.save(courses);
    }

    @Override
    public List<Course> getAll() {
        return courseDao.getAll();
    }

    @Override
    public Course getById(long id) {
        return courseDao.getById(id);
    }

    @Override
    public List<Teacher> getTeachers(Course course) {
        return courseDao.getTeachers(course);
    }

    @Override
    public List<Group> getGroups(Course course) {
        return courseDao.getGroups(course);
    }

    @Override
    public void addGroup(Course course, Group group) {
        groupDao.addToCourses(group);
    }

    @Override
    public void removeGroup(Course course, Group group) {
        groupDao.deleteFromCourse(group, course);
    }

    @Override
    public void addTeacher(Course course, Teacher teacher) {
        Teacher teacherForCourse = new Teacher();
        teacherForCourse.setId(teacher.getId());
        teacherForCourse.setFirstName(teacher.getFirstName());
        teacherForCourse.setLastName(teacher.getLastName());
        teacherForCourse.setCourses(Collections.singletonList(course));
        teacherDao.addToCourses(teacherForCourse);
    }

    @Override
    public void removeTeacher(Course course, Teacher teacher) {
        teacherDao.removeCourse(teacher, course);
    }

}
