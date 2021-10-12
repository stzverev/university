package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;

@Component
public class StudentMapper implements RowMapper<Student> {

    @Autowired
    private RowMapper<Group> groupMapper;

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(rs.getLong("student_id"));
        student.setFirstName(rs.getString("student_first_name"));
        student.setLastName(rs.getString("student_last_name"));
        Group group = groupMapper.mapRow(rs, rowNum);
        student.setGroup(group);
        return student;
    }

}
