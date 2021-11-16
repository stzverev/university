package ua.com.foxminded.university.data.service;

import java.util.List;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;

public interface GroupService extends CommonService<Group>,
    TabletimeService<Group> {

    void addToCourses(Group group);

    void removeFromCourse(Group group, Course course);

    List<Student> getStudents(Group group);

}
