package ua.com.foxminded.university.data.db.dao;

import java.time.LocalDateTime;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Tabletime;
import ua.com.foxminded.university.data.model.Teacher;

public interface TabletimeDao {

    void saveTabletime(Tabletime tabletime);

    Tabletime getTabletime(Course course, LocalDateTime begin,
            LocalDateTime end);

    Tabletime getTabletime(Group group, LocalDateTime begin,
            LocalDateTime end);

    Tabletime getTabletime(Teacher teacher, LocalDateTime begin,
            LocalDateTime end);

}
