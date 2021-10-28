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
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.db.dao.jdbc.mappers.GenericMapper;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.TabletimeRow;

@Repository
public class CourseDaoJdbc implements CourseDao {

    private static String COURSES_INSERT;
    private static String COURSES_SELECT;
    private static String COURSES_SELECT_BY_ID;
    private static String COURSES_SELECT_BY_NAME;
    private static String COURSES_UPDATE;
    private static String TABLETIME_INSERT;
    private static String TABLETIME_SELECT_BY_COURSE;
    private static String TABLETIME_UPDATE;

    private RowMapper<Course> courseMapper;
    private GenericMapper<TabletimeRow> tabletimeRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${courses.insert}")
    public void setCoursesInsert(String coursesInsert) {
        CourseDaoJdbc.COURSES_INSERT = coursesInsert;
    }

    @Value("${courses.select}")
    public void setCoursesSelect(String coursesSelect) {
        CourseDaoJdbc.COURSES_SELECT = coursesSelect;
    }

    @Value("${courses.select} WHERE id = :id")
    public void setCoursesGetById(String coursesGetById) {
        CourseDaoJdbc.COURSES_SELECT_BY_ID = coursesGetById;
    }

    @Value("${courses.select} WHERE name = :name")
    public void setCoursesGetByName(String coursesGetByName) {
        CourseDaoJdbc.COURSES_SELECT_BY_NAME = coursesGetByName;
    }

    @Value("${courses.update}")
    public void setCoursesUpdate(String coursesUpdate) {
        CourseDaoJdbc.COURSES_UPDATE = coursesUpdate;
    }

    @Value("${tabletime.insert}")
    public void setTabletimeInsert(String tabletimeInsert) {
        CourseDaoJdbc.TABLETIME_INSERT = tabletimeInsert;
    }

    @Autowired
    public void setTabletimeRowMapper(
            GenericMapper<TabletimeRow> tabletimeRowMapper) {
        this.tabletimeRowMapper = tabletimeRowMapper;
    }

    @Value(""
            + "${tabletime.select} WHERE"
            + " tabletime.date_time BETWEEN :begin AND :end"
            + " AND tabletime.course_id = :courseId")
    public void setGetTabletimeForCourse(String getTabletimeForCourse) {
        CourseDaoJdbc.TABLETIME_SELECT_BY_COURSE = getTabletimeForCourse;
    }

    @Value("${tabletime.update} WHERE course_id = :courseId")
    public void setTabletimeUpdate(String tabletimeUpdate) {
        TABLETIME_UPDATE = tabletimeUpdate;
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
    public Course getById(long id) {
        SqlParameterSource nameParameters = new MapSqlParameterSource("id", id);
        return this.jdbcTemplate.queryForObject(
                COURSES_SELECT_BY_ID, nameParameters, courseMapper::mapRow);
    }

    @Override
    public Course getByName(String name) {
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "name", name);
        return this.jdbcTemplate.queryForObject(
                COURSES_SELECT_BY_NAME, nameParameters, courseMapper::mapRow);
    }

    @Override
    public List<Course> getAll() {
        return this.jdbcTemplate.query(COURSES_SELECT,
                courseMapper::mapRow);
    }

    @Override
    public void save(Course course) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                course);
        this.jdbcTemplate.update(COURSES_INSERT, namedParameters);
    }

    @Override
    public void save(List<Course> courses) {
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(courses);
        this.jdbcTemplate.batchUpdate(COURSES_INSERT, batch);
    }

    @Override
    public void update(Course course) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                course);
        this.jdbcTemplate.update(COURSES_UPDATE, namedParameters);
    }

    @Override
    public void saveTabletime(List<TabletimeRow> tabletimeRows) {
        List<Map<String, Object>> rows = tabletimeRows
                .stream()
                .map(tabletimeRowMapper::mapToSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(rows);
        jdbcTemplate.batchUpdate(TABLETIME_INSERT, batch);
    }

    @Override
    public List<TabletimeRow> getTabletime(Course course, LocalDateTime begin,
            LocalDateTime end) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("courseId", course.getId());
        return jdbcTemplate.query(
                TABLETIME_SELECT_BY_COURSE, parameters,
                tabletimeRowMapper::mapRow);
    }

    @Override
    public void updateTabletime(List<TabletimeRow> tabletimeRows) {
        List<Map<String, Object>> rows = tabletimeRows
                .stream()
                .map(tabletimeRowMapper::mapToUpdate)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(rows);
        jdbcTemplate.batchUpdate(TABLETIME_UPDATE, batch);
    }

}
