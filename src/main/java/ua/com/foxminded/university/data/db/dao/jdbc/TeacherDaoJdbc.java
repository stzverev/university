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
import ua.com.foxminded.university.data.db.dao.jdbc.mappers.GenericMapper;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;

@Repository
public class TeacherDaoJdbc implements TeacherDao {

    private String teachersSelect;
    private String teachersInsert;
    private String teacherCoursesInsert;
    private String teachersGetById;
    private String teachersGetByFullName;
    private String teachersGetCourses;
    private String teachersUpdate;
    private String tabletimeInsert;
    private String getTabletimeForTeacher;
    private String tabletimeUpdate;

    private RowMapper<Teacher> teacherMapper;
    private RowMapper<Course> courseMapper;
    private GenericMapper<TabletimeRow> tabletimeRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${teachers.select}")
    public void setTeachersSelect(String teachersSelect) {
        this.teachersSelect = teachersSelect;
    }

    @Value("${teachers.insert}")
    public void setTeachersInsert(String teachersInsert) {
        this.teachersInsert = teachersInsert;
    }

    @Value("${teachers_courses.insert}")
    public void setTeacherCoursesInsert(String teacherCoursesInsert) {
        this.teacherCoursesInsert = teacherCoursesInsert;
    }

    @Value("${teachers.select} WHERE id = :id")
    public void setTeachersGetById(String teachersGetById) {
        this.teachersGetById = teachersGetById;
    }

    @Value(""
            + "${teachers.select}"
            + " WHERE first_Name = :firstName"
            + " AND last_name = :lastName")
    public void setTeachersGetByFullName(String teachersGetByFullName) {
        this.teachersGetByFullName = teachersGetByFullName;
    }

    @Value("${teachers_courses.select}  WHERE teachers.id = :id")
    public void setTeachersGetCourses(String teachersGetCourses) {
        this.teachersGetCourses = teachersGetCourses;
    }

    @Value("${teachers.update}")
    public void setTeachersUpdate(String teachersUpdate) {
        this.teachersUpdate = teachersUpdate;
    }

    @Value("${tabletime.insert}")
    public void setTabletimeInsert(String tabletimeInsert) {
        this.tabletimeInsert = tabletimeInsert;
    }

    @Value(""
            + "${tabletime.select} WHERE"
            + " tabletime.date_time BETWEEN :begin AND :end"
            + " AND tabletime.teacher_id = :teacherId")
    public void setGetTabletimeForTeacher(String getTabletimeForTeacher) {
        this.getTabletimeForTeacher = getTabletimeForTeacher;
    }

    @Value("${tabletime.update} WHERE teacher_id = :teacherId")
    public void setTabletimeUpdate(String tabletimeUpdate) {
        this.tabletimeUpdate = tabletimeUpdate;
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
            GenericMapper<TabletimeRow> tabletimeRowMapper) {
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
                teachersGetById, nameParameters, teacherMapper::mapRow);
    }

    @Override
    public Teacher getByFullName(String firstName, String lastName) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("firstName", firstName);
        namedParameters.put("lastName", lastName);
        return jdbcTemplate.queryForObject(
                teachersGetByFullName, namedParameters, teacherMapper::mapRow);
    }

    @Override
    public List<Course> getCourses(Teacher teacher) {
        MapSqlParameterSource namedParameters =
                new MapSqlParameterSource("id", teacher.getId());
        return jdbcTemplate.query(teachersGetCourses, namedParameters,
                courseMapper::mapRow);
    }

    @Override
    public List<Teacher> getAll() {
        return this.jdbcTemplate.query(teachersSelect,
                teacherMapper::mapRow);
    }

    @Override
    public void save(Teacher teacher) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                teacher);
        this.jdbcTemplate.update(teachersInsert, namedParameters);
    }

    @Override
    public void save(List<Teacher> teachers) {
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(teachers);
        this.jdbcTemplate.batchUpdate(teachersInsert, batch);
    }

    @Override
    public void update(Teacher teacher) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                teacher);
        this.jdbcTemplate.update(teachersUpdate, namedParameters);
    }

    @Override
    public void saveCourses(Teacher teacher) {
        List<Map<String, Long>> list = new ArrayList<>();

        for (Course course : teacher.getCourses()) {
            HashMap<String, Long> map = new HashMap<>();
            map.put("teacher_id", teacher.getId());
            map.put("course_id", course.getId());
            list.add(map);
        }
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list);
        jdbcTemplate.batchUpdate(teacherCoursesInsert, batch);
    }

    @Override
    public void saveTabletime(List<TabletimeRow> rows) {
        List<Map<String, Object>> mapRows = rows
                .stream()
                .map(tabletimeRowMapper::mapToSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(
                mapRows);
        jdbcTemplate.batchUpdate(tabletimeInsert, batch);
    }

    @Override
    public void updateTabletime(List<TabletimeRow> rows) {
        List<Map<String, Object>> mapRows = rows
                .stream()
                .map(tabletimeRowMapper::mapToUpdate)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(
                mapRows);
        jdbcTemplate.batchUpdate(tabletimeUpdate, batch);
    }

    @Override
    public List<TabletimeRow> getTabletime(Teacher teacher, LocalDateTime begin,
            LocalDateTime end) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("teacherId", teacher.getId());
        return jdbcTemplate.query(
                getTabletimeForTeacher, parameters,
                tabletimeRowMapper::mapRow);
    }

}
