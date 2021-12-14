package ua.com.foxminded.university.data.service;

import java.util.Set;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Teacher;

public interface CourseService extends CommonService<Course> {

    Set<Teacher> getTeachers(Course course);

    Set<Group> getGroups(Course course);

    void addGroup(Course course, Group group);

    void removeGroupFromCourse(Course course, Group group);

    void addTeacher(Course course, Teacher teacher);

    void removeTeacherFromCourse(Course course, Teacher teacher);

}
