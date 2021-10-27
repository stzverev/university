package ua.com.foxminded.university.data.service.beans;

import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    public TeacherServiceImpl(TeacherDao teacherDao) {
        super();
        this.teacherDao = teacherDao;
    }

    @Override
    public void save(Teacher students) {
        teacherDao.save(students);
    }

    @Override
    public void save(List<Teacher> studnets) {
        teacherDao.save(studnets);
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

}
