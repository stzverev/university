package ua.com.foxminded.university.data.service.beans;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    private CourseDao courseDao;
    private TeacherDao teacherDao;
    private GroupDao groupDao;
    private final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

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
        logger.debug("The saving of course has started: {}", course);
        try {
            courseDao.save(course);
        } catch (Exception e) {
            logger.error("Error when saving course:%n course: {};%n error: {}",
                    course, e.getMessage());
        }
    }

    @Override
    public void save(List<Course> courses) {
        logger.debug("The saving of list courses has started: list length - {}",
                courses.size());
        courseDao.save(courses);
    }

    @Override
    public void update(Course course) {
        courseDao.update(course);
    }

    @Override
    public List<Course> getAll() {
        logger.debug("The getting of all courses has started");
        return courseDao.getAll();
    }

    @Override
    public Course getById(long id) {
        logger.debug("The getting of group by id has started. id: {}", id);
        return courseDao.getById(id);
    }

    @Override
    public List<Teacher> getTeachers(Course course) {
        logger.debug("The getting of course's teachers has started: {}",
                course);
        return courseDao.getTeachers(course);
    }

    @Override
    public List<Group> getGroups(Course course) {
        logger.debug("The getting course's groups has started. {}", course);
        return courseDao.getGroups(course);
    }

    @Override
    public void addGroup(Course course, Group group) {
        logger.debug(""
                + "The adding of group to the course has started:%n"
                + "{} {}", group, course);
        groupDao.addToCourses(group);
    }

    @Override
    public void removeGroup(Course course, Group group) {
        logger.debug(""
                + "The removing of group from the course has started:%n"
                + "{} {}", group, course);
        try {
            groupDao.deleteFromCourse(group, course);
        } catch (Exception e) {
            logger.error(""
                    + "Error when removing group from course:%n"
                    + "group: {};%n"
                    + "course: {}%n"
                    + "error: {}", group, course, e.getMessage());
        }
    }

    @Override
    public void addTeacher(Course course, Teacher teacher) {
        logger.debug(""
                + "The adding of teacher to the course has started:"
                + "course: {};%n teacher: {}", course, teacher);
        Teacher teacherForCourse = new Teacher();
        teacherForCourse.setId(teacher.getId());
        teacherForCourse.setFirstName(teacher.getFirstName());
        teacherForCourse.setLastName(teacher.getLastName());
        teacherForCourse.setCourses(Collections.singletonList(course));
        try {
            teacherDao.addToCourses(teacherForCourse);
        } catch (Exception e) {
            logger.error(""
                    + "Error when adding teacher to the course"
                    + "course: {}%n"
                    + "teacher: {}%n"
                    + "error: {}", course, teacher, e.getMessage());
        }
    }

    @Override
    public void removeTeacherFromCourse(Course course, Teacher teacher) {
        logger.debug(""
                + "The removing of teacher from the course:%n"
                + "course: {};%n teacher: {}", course, teacher);
        try {
            teacherDao.removeCourse(teacher, course);
        } catch (Exception e) {
            logger.error(""
                    + "Error when removing the teacher from the course:%n"
                    + "teacher: {}%n"
                    + "course:{}%n"
                    + "error: {}", teacher, course, e.getMessage());
        }
    }

    @Override
    public void delete(long id) {
        courseDao.delete(id);
    }

}
