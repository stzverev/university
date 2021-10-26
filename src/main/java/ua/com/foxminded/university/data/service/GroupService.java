package ua.com.foxminded.university.data.service;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;

public interface GroupService extends CommonService<Group> {

    void addToCourses(Group group);

    void removeFromCourse(Group group, Course course);

}
