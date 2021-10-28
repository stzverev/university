package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Teacher;

@Component
public class TeacherMapper implements RowMapper<Teacher> {

    @Override
    public Teacher mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(resultSet.getLong("teacher_id"));
        teacher.setFirstName(resultSet.getString("teacher_first_name"));
        teacher.setLastName(resultSet.getString("teacher_last_name"));
        return teacher;
    }

}
