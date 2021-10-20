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

    private final String coursesInsert;
    private final String coursesSelect;
    private final String coursesGetById;
    private final String coursesGetByName;
    private final String coursesUpdate;
    private final String tabletimeInsert;
    private final String getTabletimeForCourse;
    private final String tabletimeUpdate;

    @Autowired
    private RowMapper<Course> courseMapper;

    @Autowired
    private GenericMapper<TabletimeRow> tabletimeRowMapper;

    private NamedParameterJdbcTemplate jdbcTemplate;

    public CourseDaoJdbc(
            @Value("${courses.insert}")
            String coursesInsert,

            @Value("${courses.select}")
            String coursesSelect,

            @Value("${courses.select} WHERE id = :id")
            String coursesGetById,

            @Value("${courses.select} WHERE name = :name")
            String coursesGetByName,

            @Value("${courses.update}")
            String coursesUpdate,

            @Value("${tabletime.insert}")
            String tabletimeInsert,

            @Value(""
                    + "${tabletime.select} WHERE"
                    + " tabletime.date_time BETWEEN :begin AND :end"
                    + " AND tabletime.course_id = :courseId")
            String getTabletimeForCourse,

            @Value("${tabletime.update} WHERE course_id = :courseId")
            String tabletimeUpdate) {
        this.coursesInsert = coursesInsert;
        this.coursesSelect = coursesSelect;
        this.coursesGetById = coursesGetById;
        this.coursesGetByName = coursesGetByName;
        this.coursesUpdate = coursesUpdate;
        this.tabletimeInsert = tabletimeInsert;
        this.getTabletimeForCourse = getTabletimeForCourse;
        this.tabletimeUpdate = tabletimeUpdate;
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
                coursesGetById, nameParameters, courseMapper::mapRow);
    }

    @Override
    public Course getByName(String name) {
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "name", name);
        return this.jdbcTemplate.queryForObject(
                coursesGetByName, nameParameters, courseMapper::mapRow);
    }

    @Override
    public List<Course> getAll() {
        return this.jdbcTemplate.query(coursesSelect,
                courseMapper::mapRow);
    }

    @Override
    public void save(Course course) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                course);
        this.jdbcTemplate.update(coursesInsert, namedParameters);
    }

    @Override
    public void save(List<Course> courses) {
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(courses);
        this.jdbcTemplate.batchUpdate(coursesInsert, batch);
    }

    @Override
    public void update(Course course) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                course);
        this.jdbcTemplate.update(coursesUpdate, namedParameters);
    }

    @Override
    public void saveTabletime(List<TabletimeRow> tabletimeRows) {
        List<Map<String, Object>> rows = tabletimeRows
                .stream()
                .map(tabletimeRowMapper::mapToSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(rows);
        jdbcTemplate.batchUpdate(tabletimeInsert, batch);
    }

    @Override
    public List<TabletimeRow> getTabletime(Course course, LocalDateTime begin,
            LocalDateTime end) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("courseId", course.getId());
        return jdbcTemplate.query(
                getTabletimeForCourse, parameters,
                tabletimeRowMapper::mapRow);
    }

    @Override
    public void updateTabletime(List<TabletimeRow> tabletimeRows) {
        List<Map<String, Object>> rows = tabletimeRows
                .stream()
                .map(tabletimeRowMapper::mapToSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(rows);
        jdbcTemplate.batchUpdate(tabletimeUpdate, batch);
    }

}
