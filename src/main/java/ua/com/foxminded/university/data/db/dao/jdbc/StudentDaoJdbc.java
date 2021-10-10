package ua.com.foxminded.university.data.db.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.db.dao.StudentDao;
import ua.com.foxminded.university.data.model.Student;

@Repository
public class StudentDaoJdbc implements StudentDao {

    @Value("${students.get}")
    private String queryGet;

    @Value("${students.insert}")
    private String queryInsert;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public Student getById(long id) {
        String sql = queryGet + " WHERE id = :id";
        SqlParameterSource nameParameters = new MapSqlParameterSource("id", id);
        return this.namedParameterJdbcTemplate.queryForObject(
                sql, nameParameters, this::mapStudent);
    }

    @Override
    public Student getByFullName(String firstName, String lastName) {
        String sql = queryGet +
                " WHERE first_Name = :firstName AND last_name = :lastName";
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("firstName", firstName);
        namedParameters.put("lastName", lastName);
        return namedParameterJdbcTemplate.queryForObject(
                sql, namedParameters, this::mapStudent);
    }

    @Override
    public List<Student> getAll() {
        return this.namedParameterJdbcTemplate.query(queryGet,
                this::mapStudent);
    }

    @Override
    public void save(Student student) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                student);
        this.namedParameterJdbcTemplate.update(queryInsert, namedParameters);
    }

    @Override
    public void save(List<Student> students) {
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(students);
        this.namedParameterJdbcTemplate.batchUpdate(queryInsert, batch);
    }

    private Student mapStudent(ResultSet resultSet, int rowNum)
            throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getLong("id"));
        student.setFirstName(resultSet.getString("first_name"));
        student.setLastName(resultSet.getString("last_name"));
        return student;
    }

}
