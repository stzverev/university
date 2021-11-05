package ua.com.foxminded.university.data.db.dao.jdbc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.db.dao.jdbc.mappers.BilateralMapper;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.PropertyReader;

@Repository
public class TeacherDaoJdbc implements TeacherDao {

    private static final String TEACHERS_SELECT = "teachers.select";

    private static final String TEACHERS_INSERT =
            "teachers.insert";

    private static final String TEACHERS_COURSES_INSERT =
            "teachers_courses.insert";

    private static final String TEACHERS_SELECT_BY_ID =
            "teachers.select.byId";

    private static final String TEACHERS_SELECT_BY_FULL_NAME =
            "teachers.select.byFullName";

    private static final String TEACHERS_COURSES_SELECT_BY_TEACHERID =
            "teachers_courses.select.byTeacherId";

    private static final String TEACHERS_UPDATE =
            "teachers.update";

    private static final String TABLETIME_INSERT =
            "tabletime.insert";

    private static final String TABLETIME_SELECT_BY_PERIOD_AND_TEACHER_ID =
            "tabletime.select.byPeriodAndTeacherId";

    private static final String TABLETIME_UPDATE =
            "tabletime.update.byTeacherId";

    private static final String TEACHERS_COURSES_DELETE =
            "teachers_courses.delete.byTeacherIdAndCourseId";

    private RowMapper<Teacher> teacherMapper;
    private RowMapper<Course> courseMapper;
    private BilateralMapper<TabletimeRow> tabletimeRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;
    private PropertyReader queryReader;

    @Autowired
    public void setTeacherMapper(RowMapper<Teacher> teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    @Autowired
    @Qualifier("queryReader")
    public void setPropertyReader(PropertyReader propertyReader) {
        this.queryReader = propertyReader;
    }

    @Autowired
    public void setCourseMapper(RowMapper<Course> courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Autowired
    public void setTabletimeRowMapper(
            BilateralMapper<TabletimeRow> tabletimeRowMapper) {
        this.tabletimeRowMapper = tabletimeRowMapper;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public Teacher getById(long id) {
        SqlParameterSource nameParameters = new MapSqlParameterSource("id", id);
        return this.jdbcTemplate.queryForObject(
                queryReader.getProperty(TEACHERS_SELECT_BY_ID),
                nameParameters,
                teacherMapper::mapRow);
    }

    @Override
    public Teacher getByFullName(String firstName, String lastName) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("firstName", firstName);
        namedParameters.put("lastName", lastName);
        return jdbcTemplate.queryForObject(
                queryReader.getProperty(TEACHERS_SELECT_BY_FULL_NAME),
                namedParameters,
                teacherMapper::mapRow);
    }

    @Override
    public List<Course> getCourses(Teacher teacher) {
        MapSqlParameterSource namedParameters =
                new MapSqlParameterSource("id", teacher.getId());
        return jdbcTemplate.query(
                queryReader.getProperty(TEACHERS_COURSES_SELECT_BY_TEACHERID),
                namedParameters,
                courseMapper::mapRow);
    }

    @Override
    public List<Teacher> getAll() {
        return this.jdbcTemplate.query(
                queryReader.getProperty(TEACHERS_SELECT),
                teacherMapper::mapRow);
    }

    @Override
    public void save(Teacher teacher) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                teacher);
        this.jdbcTemplate.update(
                queryReader.getProperty(TEACHERS_INSERT),
                namedParameters);
    }

    @Override
    public void save(List<Teacher> teachers) {
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(teachers);
        this.jdbcTemplate.batchUpdate(
                queryReader.getProperty(TEACHERS_INSERT),
                batch);
    }

    @Override
    public void update(Teacher teacher) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                teacher);
        this.jdbcTemplate.update(
                queryReader.getProperty(TEACHERS_UPDATE),
                namedParameters);
    }

    @Override
    public void addToCourses(Teacher teacher) {
        List<Map<String, Long>> list = new ArrayList<>();

        for (Course course : teacher.getCourses()) {
            HashMap<String, Long> map = new HashMap<>();
            map.put("teacher_id", teacher.getId());
            map.put("course_id", course.getId());
            list.add(map);
        }
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list);
        jdbcTemplate.batchUpdate(
                queryReader.getProperty(TEACHERS_COURSES_INSERT),
                batch);
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
                queryReader.getProperty(TABLETIME_UPDATE),
                batch);
    }

    @Override
    public List<TabletimeRow> getTabletime(Teacher teacher, LocalDateTime begin,
            LocalDateTime end) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("teacherId", teacher.getId());
        return jdbcTemplate.query(
                queryReader.getProperty(
                        TABLETIME_SELECT_BY_PERIOD_AND_TEACHER_ID),
                parameters,
                tabletimeRowMapper::mapRow);
    }

    @Override
    public void removeCourse(Teacher teacher, Course course) {
        Map<String, Long> parameters = new HashMap<>();
        parameters.put("teacherId", teacher.getId());
        parameters.put("courseId", course.getId());
        jdbcTemplate.update(
                queryReader.getProperty(TEACHERS_COURSES_DELETE),
                parameters);
    }

}
