package ua.com.foxminded.university.data.db.dao;

import java.time.LocalDateTime;
import java.util.List;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.model.TabletimeRow;

public interface GroupDao extends GenericDao<Group> {

    Group getByName(String name);

    List<Student> getStudents(Group group);

    void addTabletimeRows(List<TabletimeRow> rows);

    void updateTabletime(List<TabletimeRow> rows);

    List<TabletimeRow> getTabletime(Group group, LocalDateTime begin,
            LocalDateTime end);

    List<Course> getCourses(Group group);

    void addToCourses(Group group);

    void deleteFromCourse(Group group, Course course);

}
