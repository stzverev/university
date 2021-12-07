package ua.com.foxminded.university.data.db.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;

public interface TeacherDao extends PersonDao<Teacher> {

    Set<Course> getCourses(Teacher teacher);

    void addToCourses(Teacher teacher, Set<Course> courses);

    void removeCourse(Teacher teacher, Course course);

    void addTabletimeRows(List<TabletimeRow> rows);

    void updateTabletime(Set<TabletimeRow> rows);

    Set<TabletimeRow> getTabletime(Teacher teacher, LocalDateTime begin,
            LocalDateTime end);

}
