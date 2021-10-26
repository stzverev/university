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
import org.springframework.beans.factory.annotation.Value;
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

@Repository
public class TeacherDaoJdbc implements TeacherDao {

    private static String TEACHERS_SELECT;
    private static String TEACHERS_INSERT;
    private static String TEACHERS_COURSES_INSERT;
    private static String TEACHERS_SELECT_BY_ID;
    private static String TEACHERS_SELECT_BY_FULL_NAME;
    private static String TEACHERS_COURSES_SELECT_BY_TEACHERID;
    private static String TEACHERS_UPDATE;
    private static String TABLETIME_INSERT;
    private static String TABLETIME_SELECT_BY_PERIOD_AND_TEACHER_ID;
    private static String TABLETIME_UPDATE;
    private static String TEACHERS_COURSES_DELETE;

    private RowMapper<Teacher> teacherMapper;
    private RowMapper<Course> courseMapper;
    private BilateralMapper<TabletimeRow> tabletimeRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${teachers.select}")
    public void setTeachersSelect(String teachersSelect) {
        TeacherDaoJdbc.TEACHERS_SELECT = teachersSelect;
    }

    @Value("${teachers.insert}")
    public void setTeachersInsert(String teachersInsert) {
        TeacherDaoJdbc.TEACHERS_INSERT = teachersInsert;
    }

    @Value("${teachers_courses.insert}")
    public void setTeacherCoursesInsert(String teacherCoursesInsert) {
        TeacherDaoJdbc.TEACHERS_COURSES_INSERT = teacherCoursesInsert;
    }

    @Value("${teachers.select} WHERE id = :id")
    public void setTeachersGetById(String teachersGetById) {
        TeacherDaoJdbc.TEACHERS_SELECT_BY_ID = teachersGetById;
    }

    @Value(""
            + "${teachers.select}"
            + " WHERE first_Name = :firstName"
            + " AND last_name = :lastName")
    public void setTeachersGetByFullName(String teachersGetByFullName) {
        TeacherDaoJdbc.TEACHERS_SELECT_BY_FULL_NAME = teachersGetByFullName;
    }

    @Value("${teachers_courses.select}  WHERE teachers.id = :id")
    public void setTeachersGetCourses(String teachersGetCourses) {
        TeacherDaoJdbc.TEACHERS_COURSES_SELECT_BY_TEACHERID =
                teachersGetCourses;
    }

    @Value("${teachers.update}")
    public void setTeachersUpdate(String teachersUpdate) {
        TeacherDaoJdbc.TEACHERS_UPDATE = teachersUpdate;
    }

    @Value("${tabletime.insert}")
    public void setTabletimeInsert(String tabletimeInsert) {
        TeacherDaoJdbc.TABLETIME_INSERT = tabletimeInsert;
    }

    @Value(""
            + "${tabletime.select} WHERE"
            + " tabletime.date_time BETWEEN :begin AND :end"
            + " AND tabletime.teacher_id = :teacherId")
    public void setGetTabletimeForTeacher(String getTabletimeForTeacher) {
        TeacherDaoJdbc.TABLETIME_SELECT_BY_PERIOD_AND_TEACHER_ID =
                getTabletimeForTeacher;
    }

    @Value("${tabletime.update} WHERE teacher_id = :teacherId")
    public void setTabletimeUpdate(String tabletimeUpdate) {
        TeacherDaoJdbc.TABLETIME_UPDATE = tabletimeUpdate;
    }

    @Value(""
            + "${teachers_courses.delete}"
            + " WHERE teacher_id = :teacherId"
            + " AND course_id = :courseId")
    public void setTeachersCoursesDelete(String teachersCoursesDelete) {
        TeacherDaoJdbc.TEACHERS_COURSES_DELETE = teachersCoursesDelete;
    }

    @Autowired
    public void setTeacherMapper(RowMapper<Teacher> teacherMapper) {
        this.teacherMapper = teacherMapper;
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
                TEACHERS_SELECT_BY_ID, nameParameters, teacherMapper::mapRow);
    }

    @Override
    public Teacher getByFullName(String firstName, String lastName) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("firstName", firstName);
        namedParameters.put("lastName", lastName);
        return jdbcTemplate.queryForObject(
                TEACHERS_SELECT_BY_FULL_NAME, namedParameters,
                teacherMapper::mapRow);
    }

    @Override
    public List<Course> getCourses(Teacher teacher) {
        MapSqlParameterSource namedParameters =
                new MapSqlParameterSource("id", teacher.getId());
        return jdbcTemplate.query(TEACHERS_COURSES_SELECT_BY_TEACHERID,
                namedParameters, courseMapper::mapRow);
    }

    @Override
    public List<Teacher> getAll() {
        return this.jdbcTemplate.query(TEACHERS_SELECT,
                teacherMapper::mapRow);
    }

    @Override
    public void save(Teacher teacher) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                teacher);
        this.jdbcTemplate.update(TEACHERS_INSERT, namedParameters);
    }

    @Override
    public void save(List<Teacher> teachers) {
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(teachers);
        this.jdbcTemplate.batchUpdate(TEACHERS_INSERT, batch);
    }

    @Override
    public void update(Teacher teacher) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                teacher);
        this.jdbcTemplate.update(TEACHERS_UPDATE, namedParameters);
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
        jdbcTemplate.batchUpdate(TEACHERS_COURSES_INSERT, batch);
    }

    @Override
    public void addTabletimeRows(List<TabletimeRow> rows) {
        List<Map<String, Object>> mapRows = rows
                .stream()
                .map(tabletimeRowMapper::mapToSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(
                mapRows);
        jdbcTemplate.batchUpdate(TABLETIME_INSERT, batch);
    }

    @Override
    public void updateTabletime(List<TabletimeRow> rows) {
        List<Map<String, Object>> mapRows = rows
                .stream()
                .map(tabletimeRowMapper::mapToUpdate)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(
                mapRows);
        jdbcTemplate.batchUpdate(TABLETIME_UPDATE, batch);
    }

    @Override
    public List<TabletimeRow> getTabletime(Teacher teacher, LocalDateTime begin,
            LocalDateTime end) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("teacherId", teacher.getId());
        return jdbcTemplate.query(
                TABLETIME_SELECT_BY_PERIOD_AND_TEACHER_ID, parameters,
                tabletimeRowMapper::mapRow);
    }

    @Override
    public void removeCourse(Teacher teacher, Course course) {
        Map<String, Long> parameters = new HashMap<>();
        parameters.put("teacherId", teacher.getId());
        parameters.put("courseId", course.getId());
        jdbcTemplate.update(TEACHERS_COURSES_DELETE, parameters);
    }

}
