package ua.com.foxminded.university.data.db.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${courses.get}")
    private String queryGet;

    @Value("${courses.insert}")
    private String queryInsert;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public Course getById(long id) {
        String sql = queryGet + " WHERE id = :id";
        SqlParameterSource nameParameters = new MapSqlParameterSource("id", id);
        return this.namedParameterJdbcTemplate.queryForObject(
                sql, nameParameters, this::mapCourse);
    }

    @Override
    public Course getByName(String name) {
        String sql = queryGet + " WHERE name = :name";
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "name", name);
        return this.namedParameterJdbcTemplate.queryForObject(
                sql, nameParameters, this::mapCourse);
    }

    @Override
    public List<Course> getAll() {
        return this.namedParameterJdbcTemplate.query(queryGet,
                this::mapCourse);
    }

    @Override
    public void save(Course course) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                course);
        this.namedParameterJdbcTemplate.update(queryInsert, namedParameters);
    }

    @Override
    public void save(List<Course> courses) {
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(courses);
        this.namedParameterJdbcTemplate.batchUpdate(queryInsert, batch);
    }

    private Course mapCourse(ResultSet resultSet, int rowNum)
            throws SQLException {
        Course course = new Course();
        course.setId(resultSet.getLong("id"));
        course.setName(resultSet.getString("name"));
        return course;
    }

}
