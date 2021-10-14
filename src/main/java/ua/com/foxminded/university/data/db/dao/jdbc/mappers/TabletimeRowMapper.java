package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;

@Component
public class TabletimeRowMapper implements RowMapper<TabletimeRow> {

    @Autowired
    private RowMapper<Course> courseMapper;

    @Autowired
    private RowMapper<Group> groupMapper;

    @Autowired
    private RowMapper<Teacher> teacherMapper;

    @Override
    public TabletimeRow mapRow(ResultSet rs, int rowNum) throws SQLException {
        TabletimeRow tabletimeRow = new TabletimeRow();
        LocalDateTime dateTime = rs.getTimestamp("date_time").toLocalDateTime();
        tabletimeRow.setDateTime(dateTime);
        Course course = courseMapper.mapRow(rs, rowNum);
        Group group = groupMapper.mapRow(rs, rowNum);
        Teacher teacher = teacherMapper.mapRow(rs, rowNum);
        tabletimeRow.setCourse(course);
        tabletimeRow.setGroup(group);
        tabletimeRow.setTeacher(teacher);
        return tabletimeRow;
    }

}
