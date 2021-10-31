package ua.com.foxminded.university.data.db.dao.jdbc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.db.dao.jdbc.mappers.BilateralMapper;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.service.PropertyReader;

@Repository
public class GroupDaoJdbc implements GroupDao {

    private static final String GROUPS_SELECT = "groups.select";
    private static final String GROUPS_INSERT = "groups.insert";
    private static final String GROUPS_SELECT_BY_ID = "groups.select.byId";
    private static final String GROUPS_SELECT_BY_NAME = "groups.select.byName";
    private static final String STUDENTS_SELECT_BY_GROUPID =
            "students.select.byGroupId";
    private static final String GROUPS_UPDATE = "groups.update";
    private static final String TABLETIME_INSERT = "tabletime.insert";
    private static final String TABLETIME_SELECT_BY_PERIOD_AND_GROUPID =
            "tabletime.select.byPeriodAndGroupId";
    private static final String TABLETIME_UPDATE_BY_GROUPID =
            "tabletime.update.byGroupId";
    private static final String GROUPS_COURSES_SELECT_BY_GROUPID =
            "groups_courses.select.byGroupId";
    private static final String GROUPS_COURSES_INSERT = "groups_courses.insert";
    private static final String GROUPS_COURSES_DELETE =
            "groups_courses.delete.byGroupIdAndCourseId";

    private RowMapper<Group> groupMapper;
    private RowMapper<Student> studentMapper;
    private RowMapper<Course> courseMapper;
    private BilateralMapper<TabletimeRow> tabletimeRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;
    private PropertyReader queryReader;

    @Autowired
    @Qualifier("queryReader")
    public void setPropertyReader(PropertyReader queryReader) {
        this.queryReader = queryReader;
    }

    @Autowired
    public void setGroupMapper(RowMapper<Group> groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Autowired
    public void setStudentMapper(RowMapper<Student> studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Autowired
    public void setTabletimeRowMapper(
            BilateralMapper<TabletimeRow> tabletimeRowMapper) {
        this.tabletimeRowMapper = tabletimeRowMapper;
    }

    @Autowired
    public void setCourseMapper(RowMapper<Course> courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public Group getById(long id) {
        SqlParameterSource nameParameters = new MapSqlParameterSource("id", id);
        return this.jdbcTemplate.queryForObject(
                queryReader.getProperty(GROUPS_SELECT_BY_ID),
                nameParameters, groupMapper::mapRow);
    }

    @Override
    public Group getByName(String name) {
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "name", name);
        return this.jdbcTemplate.queryForObject(
                queryReader.getProperty(GROUPS_SELECT_BY_NAME),
                nameParameters, groupMapper::mapRow);
    }

    @Override
    public List<Group> getAll() {
        return this.jdbcTemplate.query(
                queryReader.getProperty(GROUPS_SELECT),
                groupMapper::mapRow);
    }

    @Override
    public void save(Group group) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                group);
        this.jdbcTemplate.update(
                queryReader.getProperty(GROUPS_INSERT),
                namedParameters);
    }

    @Override
    public void save(List<Group> groups) {
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(groups);
        this.jdbcTemplate.batchUpdate(
                queryReader.getProperty(GROUPS_INSERT),
                batch);
    }

    @Override
    public List<Student> getStudents(Group group) {
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "groupId", group.getId());
        return jdbcTemplate.query(
                queryReader.getProperty(STUDENTS_SELECT_BY_GROUPID),
                nameParameters,
                studentMapper::mapRow);
    }

    @Override
    public void update(Group group) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                group);
        this.jdbcTemplate.update(
                queryReader.getProperty(GROUPS_UPDATE),
                namedParameters);
    }

    @Override
    public List<TabletimeRow> getTabletime(Group group, LocalDateTime begin,
            LocalDateTime end) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("groupId", group.getId());
        return jdbcTemplate.query(
                queryReader.getProperty(TABLETIME_SELECT_BY_PERIOD_AND_GROUPID),
                parameters,
                tabletimeRowMapper::mapRow);
    }

    @Override
    public void addTabletimeRows(List<TabletimeRow> rows) {
        List<Map<String, Object>> mapRows = rows
                .stream()
                .map(tabletimeRowMapper::mapToSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(
                mapRows);
        jdbcTemplate.batchUpdate(
                queryReader.getProperty(TABLETIME_INSERT),
                batch);

    }

    @Override
    public void updateTabletime(List<TabletimeRow> rows) {
        List<Map<String, Object>> mapRows = rows
                .stream()
                .map(tabletimeRowMapper::mapToUpdate)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(
                mapRows);
        jdbcTemplate.batchUpdate(
                queryReader.getProperty(TABLETIME_UPDATE_BY_GROUPID),
                batch);
    }

    @Override
    public List<Course> getCourses(Group group) {
        MapSqlParameterSource parameters = new MapSqlParameterSource(
                "id", group.getId());
        return jdbcTemplate.query(
                queryReader.getProperty(GROUPS_COURSES_SELECT_BY_GROUPID),
                parameters, courseMapper);
    }

    @Override
    public void addToCourses(Group group) {
        List<Map<String, Long>> parameters = group.getCourses()
                .stream()
                .map(it -> {
                    Map<String, Long> map = new HashMap<>();
                    map.put("groupId", group.getId());
                    map.put("courseId", it.getId());
                    return map;
                })
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(parameters);
        jdbcTemplate.batchUpdate(
                queryReader.getProperty(GROUPS_COURSES_INSERT),
                batch);
    }

    @Override
    public void deleteFromCourse(Group group, Course course) {
        Map<String, Long> parameters = new HashMap<>();
        parameters.put("groupId", group.getId());
        parameters.put("courseId", course.getId());
        jdbcTemplate.update(
                queryReader.getProperty(GROUPS_COURSES_DELETE),
                parameters);
    }

}
