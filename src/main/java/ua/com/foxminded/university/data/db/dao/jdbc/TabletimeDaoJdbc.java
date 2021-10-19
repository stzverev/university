package ua.com.foxminded.university.data.db.dao.jdbc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.db.dao.TabletimeDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Tabletime;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;

@Repository
public class TabletimeDaoJdbc implements TabletimeDao {

    private final String tabletimeInsert;
    private final String getTabletimeForCourse;
    private final String getTabletimeForGroup;
    private final String getTabletimeForTeacher;

    @Autowired
    private RowMapper<TabletimeRow> tabletimeRowMapper;

    private NamedParameterJdbcTemplate jdbcTemplate;



    public TabletimeDaoJdbc(
            @Value("${tabletime.insert}") String tabletimeInsert,
            @Value(""
                    + "${tabletime.select} WHERE"
                    + " tabletime.date_time BETWEEN :begin AND :end"
                    + " AND tabletime.course_id = :courseId")
            String getTabletimeForCourse,
            @Value(""
                    + "${tabletime.select} WHERE"
                    + " tabletime.date_time BETWEEN :begin AND :end"
                    + " AND tabletime.group_id = :groupId")
            String getTabletimeForGroup,
            @Value(""
                    + "${tabletime.select} WHERE"
                    + " tabletime.date_time BETWEEN :begin AND :end"
                    + " AND tabletime.teacher_id = :teacherId")
            String getTabletimeForTeacher) {
        super();
        this.tabletimeInsert = tabletimeInsert;
        this.getTabletimeForCourse = getTabletimeForCourse;
        this.getTabletimeForGroup = getTabletimeForGroup;
        this.getTabletimeForTeacher = getTabletimeForTeacher;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Override
    public void saveTabletime(Tabletime tabletime) {
        List<Map<String, Object>> rows = tabletime.getRows()
                .stream()
                .map(this::mapParam)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(rows);
        jdbcTemplate.batchUpdate(tabletimeInsert, batch);
    }

    @Override
    public Tabletime getTabletime(Course course, LocalDateTime begin,
            LocalDateTime end) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("courseId", course.getId());
        List<TabletimeRow> list =
                getJdbcTemplate().query(
                        getTabletimeForCourse, parameters,
                        tabletimeRowMapper::mapRow);
        return new Tabletime(begin, end, list);
    }

    @Override
    public Tabletime getTabletime(Group group, LocalDateTime begin,
            LocalDateTime end) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("groupId", group.getId());
        List<TabletimeRow> list =
                getJdbcTemplate().query(
                        getTabletimeForGroup, parameters,
                        tabletimeRowMapper::mapRow);
        return new Tabletime(begin, end, list);
    }

    @Override
    public Tabletime getTabletime(Teacher teacher, LocalDateTime begin,
            LocalDateTime end) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("teacherId", teacher.getId());
        List<TabletimeRow> list =
                getJdbcTemplate().query(
                        getTabletimeForTeacher, parameters,
                        tabletimeRowMapper::mapRow);
        return new Tabletime(begin, end, list);
    }

    private Map<String, Object> mapParam(TabletimeRow row) {
        Map<String, Object> map = new HashMap<>();
        map.put("dateTime", Timestamp.valueOf(row.getDateTime()));
        map.put("groupId", row.getGroup().getId());
        map.put("teacherId", row.getTeacher().getId());
        map.put("courseId", row.getCourse().getId());
        return map;
    }

}
