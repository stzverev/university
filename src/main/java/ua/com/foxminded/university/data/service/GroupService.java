package ua.com.foxminded.university.data.service;

import java.util.Set;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;

public interface GroupService extends CommonService<Group> {

    void addToCourses(Group group, Set<Course> courses);

    void addToCourse(Group group, Course course);

    void removeFromCourse(Group group, Course course);

    Set<Student> getStudents(Group group);

    Set<Course> getCourses(Group group);

}
