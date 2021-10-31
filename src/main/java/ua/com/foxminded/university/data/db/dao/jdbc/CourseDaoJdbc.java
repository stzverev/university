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

import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.db.dao.jdbc.mappers.BilateralMapper;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.PropertyReader;

@Repository
public class CourseDaoJdbc implements CourseDao {

    private static final String COURSES_INSERT = "courses.insert";
    private static final String COURSES_SELECT = "courses.select";
    private static final String COURSES_SELECT_BY_ID = "courses.select.byId";
    private static final String COURSES_SELECT_BY_NAME =
            "courses.select.byName";
    private static final String COURSES_UPDATE = "courses.update";
    private static final String TABLETIME_INSERT = "tabletime.insert";
    private static final String TABLETIME_SELECT_BY_PERIOD_AND_COURSEID =
            "tabletime.select.byPeriodAndCourseId";
    private static final String TABLETIME_UPDATE_BY_COURSEID =
            "tabletime.update.byCourseId";
    private static final String TEACHERS_COURSES_SELECT_BY_COURSE_ID =
            "teachers_courses.select.byCourseId";
    private static final String GROUPS_COURSES_SELECT_BY_COURSE_ID =
            "groups_courses.select.byCourseId";

    private RowMapper<Course> courseMapper;
    private RowMapper<Teacher> teacherMapper;
    private RowMapper<Group> groupMapper;
    private BilateralMapper<TabletimeRow> tabletimeRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;
    private PropertyReader queryReader;

    @Autowired
    @Qualifier("queryReader")
    public void setQueryReader(PropertyReader queryReader) {
        this.queryReader = queryReader;
    }

    @Autowired
    public void setTabletimeRowMapper(
            BilateralMapper<TabletimeRow> tabletimeRowMapper) {
        this.tabletimeRowMapper = tabletimeRowMapper;
    }

    @Autowired
    public void setTeacherMapper(
            RowMapper<Teacher> teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    @Autowired
    public void setCourseMapper(RowMapper<Course> courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Autowired
    public void setGroupMapper(RowMapper<Group> groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public Course getById(long id) {
        SqlParameterSource nameParameters = new MapSqlParameterSource("id", id);
        return this.jdbcTemplate.queryForObject(
                queryReader.getProperty(COURSES_SELECT_BY_ID),
                nameParameters, courseMapper::mapRow);
    }

    @Override
    public Course getByName(String name) {
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "name", name);
        return this.jdbcTemplate.queryForObject(
                queryReader.getProperty(COURSES_SELECT_BY_NAME),
                nameParameters, courseMapper::mapRow);
    }

    @Override
    public List<Course> getAll() {
        return this.jdbcTemplate.query(
                queryReader.getProperty(COURSES_SELECT),
                courseMapper::mapRow);
    }

    @Override
    public void save(Course course) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                course);
        this.jdbcTemplate.update(
                queryReader.getProperty(COURSES_INSERT),
                namedParameters);
    }

    @Override
    public void save(List<Course> courses) {
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(courses);
        this.jdbcTemplate.batchUpdate(
                queryReader.getProperty(COURSES_INSERT),
                batch);
    }

    @Override
    public void update(Course course) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                course);
        this.jdbcTemplate.update(
                queryReader.getProperty(COURSES_UPDATE),
                namedParameters);
    }

    @Override
    public void saveTabletime(List<TabletimeRow> tabletimeRows) {
        List<Map<String, Object>> rows = tabletimeRows
                .stream()
                .map(tabletimeRowMapper::mapToSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(rows);
        jdbcTemplate.batchUpdate(
                queryReader.getProperty(TABLETIME_INSERT),
                batch);
    }

    @Override
    public List<TabletimeRow> getTabletime(Course course, LocalDateTime begin,
            LocalDateTime end) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("courseId", course.getId());
        return jdbcTemplate.query(
                queryReader.getProperty(
                        TABLETIME_SELECT_BY_PERIOD_AND_COURSEID),
                parameters,
                tabletimeRowMapper::mapRow);
    }

    @Override
    public void updateTabletime(List<TabletimeRow> tabletimeRows) {
        List<Map<String, Object>> rows = tabletimeRows
                .stream()
                .map(tabletimeRowMapper::mapToUpdate)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(rows);
        jdbcTemplate.batchUpdate(
                queryReader.getProperty(TABLETIME_UPDATE_BY_COURSEID),
                batch);
    }

    @Override
    public List<Teacher> getTeachers(Course course) {
        MapSqlParameterSource parameters = new MapSqlParameterSource(
                "id", course.getId());
        return jdbcTemplate.query(
                queryReader.getProperty(TEACHERS_COURSES_SELECT_BY_COURSE_ID),
                parameters, teacherMapper);
    }

    @Override
    public List<Group> getGroups(Course course) {
        MapSqlParameterSource parameters = new MapSqlParameterSource(
                "id", course.getId());
        return jdbcTemplate.query(
                queryReader.getProperty(GROUPS_COURSES_SELECT_BY_COURSE_ID),
                parameters, groupMapper);
    }

}
