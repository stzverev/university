package ua.com.foxminded.university.data.service;

import java.time.LocalDateTime;
import java.util.List;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;

public interface TeacherService extends CommonService<Teacher> {

    void addCourses(Teacher teacher);

    void removeCourse(Teacher teacher, Course course);

    List<TabletimeRow> getTabletime(Teacher teacher, LocalDateTime begin,
            LocalDateTime end);

    void addTabletimeRows(List<TabletimeRow> tabletimeRows);

}
