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
import ua.com.foxminded.university.data.model.Tabletime;
import ua.com.foxminded.university.data.model.Teacher;

@Repository
public class TeacherDaoJdbc implements TeacherDao {

    @Value("${teachers.select}")
    private String teachersSelect;

    @Value("${teachers.insert}")
    private String teachersInsert;

    @Value("${teachers_courses.select}")
    private String teachersCoursesSelect;

    @Value("${teachers_courses.insert}")
    private String teacherCoursesInsert;

    @Autowired
    private RowMapper<Teacher> teacherMapper;

    @Autowired
    private RowMapper<Course> courseMapper;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public Teacher getById(long id) {
        String sql = teachersSelect + " WHERE id = :id";
        SqlParameterSource nameParameters = new MapSqlParameterSource("id", id);
        return this.jdbcTemplate.queryForObject(
                sql, nameParameters, teacherMapper::mapRow);
    }

    @Override
    public Teacher getByFullName(String firstName, String lastName) {
        String sql = teachersSelect +
                " WHERE first_Name = :firstName AND last_name = :lastName";
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("firstName", firstName);
        namedParameters.put("lastName", lastName);
        return jdbcTemplate.queryForObject(
                sql, namedParameters, teacherMapper::mapRow);
    }

    @Override
    public List<Course> getCourses(Teacher teacher) {
        String sql = teachersCoursesSelect + " WHERE teachers.id = :id";
        MapSqlParameterSource namedParameters =
                new MapSqlParameterSource("id", teacher.getId());
        return jdbcTemplate.query(sql, namedParameters,
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

    @Override
    public Tabletime getTabletime(Teacher teacher) {
        // TODO Auto-generated method stub
        return null;
    }

}
