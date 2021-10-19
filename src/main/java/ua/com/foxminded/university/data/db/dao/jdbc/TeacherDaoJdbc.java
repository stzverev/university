package ua.com.foxminded.university.data.db.dao.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;

@Repository
public class TeacherDaoJdbc implements TeacherDao {

    private final String teachersSelect;
    private final String teachersInsert;
    private final String teacherCoursesInsert;
    private final String teachersGetById;
    private final String teachersGetByFullName;
    private final String teachersGetCourses;

    @Autowired
    private RowMapper<Teacher> teacherMapper;

    @Autowired
    private RowMapper<Course> courseMapper;

    private NamedParameterJdbcTemplate jdbcTemplate;

    public TeacherDaoJdbc(@Value("${teachers.select}") String teachersSelect,
            @Value("${teachers.insert}") String teachersInsert,
            @Value("${teachers_courses.insert}") String teacherCoursesInsert,
            @Value("${teachers.select} WHERE id = :id") String teachersGetById,
            @Value(""
                    + "${teachers.select}"
                    + " WHERE first_Name = :firstName"
                    + " AND last_name = :lastName")
            String teachersGetByFullName,
            @Value("${teachers_courses.select}  WHERE teachers.id = :id")
            String teachersGetCourses) {
        super();
        this.teachersSelect = teachersSelect;
        this.teachersInsert = teachersInsert;
        this.teacherCoursesInsert = teacherCoursesInsert;
        this.teachersGetById = teachersGetById;
        this.teachersGetByFullName = teachersGetByFullName;
        this.teachersGetCourses = teachersGetCourses;
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

}
