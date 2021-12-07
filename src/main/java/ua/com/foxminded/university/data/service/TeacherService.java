package ua.com.foxminded.university.data.service;

import java.util.Set;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;

public interface TeacherService extends CommonService<Teacher>,
    TabletimeService<Teacher> {

    void addCourses(Teacher teacher, Set<Course> courses);

    Set<Course> getCourses(Teacher teacher);

    void removeCourse(Teacher teacher, Course course);

    void removeFromCourse(Teacher teacher, Course course);

    void addToCourse(Teacher teacher, Course course);

}
