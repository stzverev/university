package ua.com.foxminded.university.data.service;

import java.util.List;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;

public interface TeacherService extends CommonService<Teacher>,
    TabletimeService<Teacher> {

    void addCourses(Teacher teacher);

    List<Course> getCourses(Teacher teacher);

    void removeCourse(Teacher teacher, Course course);

    void removeFromCourse(Teacher teacher, Course course);

    void addToCourses(Teacher teacher);

}
