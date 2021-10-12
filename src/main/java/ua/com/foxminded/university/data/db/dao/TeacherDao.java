package ua.com.foxminded.university.data.db.dao;

import java.util.List;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Tabletime;
import ua.com.foxminded.university.data.model.Teacher;

public interface TeacherDao extends PersonDao<Teacher> {

    List<Course> getCourses(Teacher teacher);

    Tabletime getTabletime(Teacher teacher);

    void saveCourses(Teacher teacher);

}
