package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;

@Component
public class StudentMapper implements GenericMapper<Student> {

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

    @Override
    public Map<String, Object> mapToSave(Student student) {
        return commonParameters(student);
    }

    @Override
    public Map<String, Object> mapToUpdate(Student student) {
        Map<String, Object> namedParameters = commonParameters(student);
        namedParameters.put("id", student.getId());
        return namedParameters;
    }

    private Map<String, Object> commonParameters(Student student) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("firstName", student.getFirstName());
        namedParameters.put("lastName", student.getLastName());
        namedParameters.put("groupId", student.getGroup().getId());
        return namedParameters;
    }

}
