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
        logger.debug("The saving of teacher has started: {}", teacher);
        try {
            teacherDao.save(teacher);
        } catch (Exception e) {
            logger.error(""
                    + "Error when saving the teacher: teacher: {}; "
                    + "error: {}", teacher, e.getMessage());
            throw e;
        }
    }

    @Override
    public void save(List<Teacher> teachers) {
        logger.debug("The saving of list teachers has started:%n teachers count - {}",
                teachers.size());
        try {
            teacherDao.save(teachers);
        } catch (Exception e) {
            logger.error(""
                    + "Error when saving list teachers:%n teachers count - {}%n"
                    + "error: {}", teachers.size(), e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Teacher> getAll() {
        logger.debug("The getting of all teachers has started");
        return teacherDao.getAll();
    }

    @Override
    public Teacher getById(long id) {
        logger.debug("The getting of teacher by id has started:%n id: {}", id);
        return teacherDao.getById(id);
    }

    @Override
    public void addCourses(Teacher teacher) {
        logger.debug(""
                + "The adding of courses to the teacher has started:%n"
                + "teacher: {}%n"
                + "courses count: {}", teacher, teacher.getCourses().size());
        try {
            teacherDao.addToCourses(teacher);
        } catch (Exception e) {
            logger.error(""
                    + "Error when adding the teacher to the courses:%n"
                    + "teacher: {};%n"
                    + "courses count: {};%n"
                    + "error: {}", teacher, teacher.getCourses().size(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeCourse(Teacher teacher, Course course) {
        logger.debug(""
                + "The removing of teacher from courses:%n"
                + "teacher: {};%n"
                + "course: {}", teacher, course);
        try {
            teacherDao.removeCourse(teacher, course);
        } catch (Exception e) {
            logger.error(""
                    + "Error when removing the teacher from the course:%n"
                    + "teacher: {};%n"
                    + "course: {};%n"
                    + "error: {}", teacher, course, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<TabletimeRow> getTabletime(Teacher teacher, LocalDateTime begin,
            LocalDateTime end) {
        logger.debug(""
                + "The getting of tabletime has started%n"
                + "teacher: {}; begin: {}; end: {}",
                teacher, begin, end);
        return teacherDao.getTabletime(teacher, begin, end);
    }

    @Override
    public void addTabletimeRows(List<TabletimeRow> tabletimeRows) {
        logger.debug("The adding of tabletime rows has started:%n rows count - {}",
                tabletimeRows.size());
        try {
            teacherDao.addTabletimeRows(tabletimeRows);
        } catch (Exception e) {
            logger.error(""
                    + "Error when adding tabletime rows: rows count - {};%n error: {}",
                    tabletimeRows.size(), e.getMessage());
            throw e;
        }
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
