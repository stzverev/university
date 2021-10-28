package ua.com.foxminded.university.data.db.dao;

import java.time.LocalDateTime;
import java.util.List;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.TabletimeRow;

public interface CourseDao extends GenericDao<Course> {

    Course getByName(String name);

    void saveTabletime(List<TabletimeRow> rows);

    void updateTabletime(List<TabletimeRow> rows);

    List<TabletimeRow> getTabletime(Course course, LocalDateTime begin,
            LocalDateTime end);

}
