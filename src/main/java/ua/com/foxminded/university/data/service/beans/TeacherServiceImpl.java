package ua.com.foxminded.university.data.service.beans;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private final Logger logger = LoggerFactory.getLogger(
            TeacherServiceImpl.class);

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
                    + "Error when saving the teacher:%n teacher: {};%n"
                    + "error: {}", teacher, e.getMessage());
        }
    }

    @Override
    public void save(List<Teacher> teachers) {
        String teachersDescription = teachers
                .stream()
                .map(Teacher::toString)
                .collect(Collectors.joining("; " + System.lineSeparator()));
        logger.debug("The saving of list teachers has started:%n {}",
                teachersDescription);
        try {
            teacherDao.save(teachers);
        } catch (Exception e) {
            logger.error(""
                    + "Error when saving list teachers:%n students:{}%n"
                    + "error: {}", teachersDescription, e.getMessage());
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
        String coursesDescription = teacher.getCourses()
                .stream()
                .map(Course::toString)
                .collect(Collectors.joining("; " + System.lineSeparator()));
        logger.debug(""
                + "The adding of courses to the teacher has started:%n"
                + "teacher: {}%n"
                + "courses: {}", teacher, coursesDescription);
        try {
            teacherDao.addToCourses(teacher);
        } catch (Exception e) {
            logger.error(""
                    + "Error when adding the teacher to the courses:%n"
                    + "teacher: {};%n"
                    + "courses: {};%n"
                    + "error: {}", teacher, coursesDescription, e.getMessage());
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
        String tabletimeRowsDescription = tabletimeRows
                .stream()
                .map(TabletimeRow::toString)
                .collect(Collectors.joining("; " + System.lineSeparator()));
        logger.debug("The adding of tabletime rows has started:%n{}",
                tabletimeRowsDescription);
        try {
            teacherDao.addTabletimeRows(tabletimeRows);
        } catch (Exception e) {
            logger.error(""
                    + "Error when adding tabletime rows: {};%n error: {}",
                    tabletimeRowsDescription, e.getMessage());
        }
    }

}
