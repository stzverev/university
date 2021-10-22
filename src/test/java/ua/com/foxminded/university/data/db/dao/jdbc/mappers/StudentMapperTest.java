package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;

@SpringJUnitConfig(Config.class)
@ExtendWith(MockitoExtension.class)
class StudentMapperTest {

    private StudentMapper studentMapper;

    @Autowired
    private void setStudentMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Test
    void shouldGetStudentWhenMapResultSet() throws SQLException {
        Group group = new Group();
        group.setId(1);
        group.setName("B1");
        Student student = new Student();
        student.setFirstName("Sheldon");
        student.setLastName("Cooper");
        student.setGroup(group);
        student.setId(1);
        Student expected = student;

        ResultSet resultSet = getStudentResultSetMock(student);
        Student actual = studentMapper.mapRow(resultSet, 1);

        assertEquals(expected, actual);
    }

    private ResultSet getStudentResultSetMock(Student student) throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("student_id")).thenReturn(student.getId());
        when(resultSet.getString("student_first_name")).thenReturn(
                student.getFirstName());
        when(resultSet.getString("student_last_name")).thenReturn(
                student.getLastName());
        when(resultSet.getLong("group_id")).thenReturn(
                student.getGroup().getId());
        when(resultSet.getString("group_name")).thenReturn(
                student.getGroup().getName());
        return resultSet;
    }

}
