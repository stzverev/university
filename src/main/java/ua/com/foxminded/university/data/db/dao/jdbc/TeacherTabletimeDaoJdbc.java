package ua.com.foxminded.university.data.db.dao.jdbc;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;

import ua.com.foxminded.university.data.model.Tabletime;
import ua.com.foxminded.university.data.model.Teacher;

public class TeacherTabletimeDaoJdbc extends TabletimeDaoJdbc<Teacher> {

    @Value("&{tabletime.select}")
    protected String tabletimeSelect;

    @Override
    public Tabletime getTabletime(Teacher teacher, LocalDate begin,
            LocalDate end) {
        return null;
    }

}
