package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;

@Component
public class TabletimeRowMapper implements BilateralMapper<TabletimeRow> {

    private RowMapper<Course> courseMapper;
    private RowMapper<Group> groupMapper;
    private RowMapper<Teacher> teacherMapper;

    @Autowired
    public void setCourseMapper(RowMapper<Course> courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Autowired
    public void setGroupMapper(RowMapper<Group> groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Autowired
    public void setTeacherMapper(RowMapper<Teacher> teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

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

    @Override
    public Map<String, Object> mapToSave(TabletimeRow row) {
        Map<String, Object> map = new HashMap<>();
        map.put("dateTime", Timestamp.valueOf(row.getDateTime()));
        map.put("groupId", row.getGroup().getId());
        map.put("teacherId", row.getTeacher().getId());
        map.put("courseId", row.getCourse().getId());
        return map;
    }

    @Override
    public Map<String, Object> mapToUpdate(TabletimeRow row) {
        return mapToSave(row);
    }

}
