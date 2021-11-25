package ua.com.foxminded.university.data.service.beans;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.TeacherService;

@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherDao teacherDao;
    private final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

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
        return teacherDao.getById(id);
    }

    @Override
    public void addCourses(Teacher teacher) {
        teacherDao.addToCourses(teacher);
    }

    @Override
    public void removeCourse(Teacher teacher, Course course) {
        teacherDao.removeCourse(teacher, course);
    }

    @Override
    public List<TabletimeRow> getTabletime(Teacher teacher, LocalDateTime begin,
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
    public List<Course> getCourses(Teacher teacher) {
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
    public void addToCourses(Teacher teacher) {
        teacherDao.addToCourses(teacher);
    }

}
