package ua.com.foxminded.university.data.db.dao.jdbc;

import java.util.List;

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
import ua.com.foxminded.university.data.model.Course;

@Repository
public class CourseDaoJdbc implements CourseDao {

    @Value("${courses.select}")
    private String coursesSelect;

    @Value("${courses.insert}")
    private String coursesInsert;

    @Autowired
    private RowMapper<Course> courseMapper;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public Course getById(long id) {
        String sql = coursesSelect + " WHERE id = :id";
        SqlParameterSource nameParameters = new MapSqlParameterSource("id", id);
        return this.jdbcTemplate.queryForObject(
                sql, nameParameters, courseMapper::mapRow);
    }

    @Override
    public Course getByName(String name) {
        String sql = coursesSelect + " WHERE name = :name";
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "name", name);
        return this.jdbcTemplate.queryForObject(
                sql, nameParameters, courseMapper::mapRow);
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

}
