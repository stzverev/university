package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.ConfigTest;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;

@SpringJUnitConfig(ConfigTest.class)
@ExtendWith(MockitoExtension.class)
class TabletimeRowMapperTest {

    private TabletimeRowMapper tabletimeMapper;

    @Autowired
    private void setTabletimeRowMapper(TabletimeRowMapper tabletimeMapper) {
        this.tabletimeMapper = tabletimeMapper;
    }

    @Test
    void shouldGetTabletimeRowWhenMapResultSet() throws SQLException {
        Course course = new Course();
        course.setName("Math");
        LocalDateTime dateTime = LocalDateTime.of(2021, 10, 22, 0, 0);
        Group group = new Group();
        group.setName("HR-12");
        Teacher teacher = new Teacher();
        teacher.setFirstName("Leonard");
        teacher.setLastName("Hofstadter");

        TabletimeRow tabletimeRow = new TabletimeRow();
        tabletimeRow.setCourse(course);
        tabletimeRow.setDateTime(dateTime);
        tabletimeRow.setGroup(group);
        tabletimeRow.setCourse(course);
        tabletimeRow.setTeacher(teacher);
        TabletimeRow expected = tabletimeRow;

        ResultSet resultSet = getTabletimeRowResultSetMock(tabletimeRow);
        TabletimeRow actual = tabletimeMapper.mapRow(resultSet, 1);

        assertEquals(expected, actual);
    }

    private ResultSet getTabletimeRowResultSetMock(TabletimeRow tabletimeRow) throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getTimestamp("tabletime_datetime")).thenReturn(
                Timestamp.valueOf(tabletimeRow.getDateTime()));
        when(resultSet.getString("teacher_first_name")).thenReturn(
                tabletimeRow.getTeacher().getFirstName());
        when(resultSet.getString("teacher_last_name")).thenReturn(
                tabletimeRow.getTeacher().getLastName());
        when(resultSet.getString("course_name")).thenReturn(
                tabletimeRow.getCourse().getName());
        when(resultSet.getString("group_name")).thenReturn(
                tabletimeRow.getGroup().getName());
        return resultSet;
    }

}
