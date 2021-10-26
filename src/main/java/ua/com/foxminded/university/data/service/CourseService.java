package ua.com.foxminded.university.data.service;

import java.util.List;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Teacher;

interface CourseService extends CommonService<Course> {

    List<Teacher> getTeachers(Course course);

    List<Group> getGroups(Course course);

    void addGroup(Course course, Group group);

    void removeGroup(Course course, Group group);

    void addTeacher(Course course, Teacher teacher);

    void removeTeacher(Course course, Teacher teacher);

}
