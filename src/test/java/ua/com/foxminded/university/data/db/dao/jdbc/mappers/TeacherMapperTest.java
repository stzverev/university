package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.model.Teacher;

@SpringJUnitConfig(Config.class)
@ExtendWith(MockitoExtension.class)
class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @Autowired
    private void setTeacherMapper(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    @Test
    void shouldGetTeacherWhenMapResultSet() throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Sheldon");
        teacher.setLastName("Cooper");
        teacher.setId(1);
        Teacher expected = teacher;

        ResultSet resultSet = getTeacherResultSetMock(teacher);
        Teacher actual = teacherMapper.mapRow(resultSet, 1);

        assertEquals(expected, actual);
    }

    private ResultSet getTeacherResultSetMock(Teacher teacher) throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getLong("teacher_id")).thenReturn(teacher.getId());
        when(resultSet.getString("teacher_first_name")).thenReturn(
                teacher.getFirstName());
        when(resultSet.getString("teacher_last_name")).thenReturn(
                teacher.getLastName());
        return resultSet;
    }

}
