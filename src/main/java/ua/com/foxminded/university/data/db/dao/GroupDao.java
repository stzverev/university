package ua.com.foxminded.university.data.db.dao;

import java.time.LocalDateTime;
import java.util.Set;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.model.TabletimeRow;

public interface GroupDao extends GenericDao<Group> {

    Group getByName(String name);

    Set<Student> getStudents(Group group);

    void addTabletimeRows(Set<TabletimeRow> rows);

    void updateTabletime(Set<TabletimeRow> rows);

    Set<TabletimeRow> getTabletime(Group group, LocalDateTime begin,
            LocalDateTime end);

    Set<Course> getCourses(Group group);

    void addToCourses(Group group, Set<Course> courses);

    void deleteFromCourse(Group group, Course course);

}
