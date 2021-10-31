package ua.com.foxminded.university.data.service;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;

public interface TeacherService extends CommonService<Teacher>,
    TabletimeService<Teacher> {

    void addCourses(Teacher teacher);

    void removeCourse(Teacher teacher, Course course);

}
