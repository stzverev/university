package ua.com.foxminded.university.data.db.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.db.dao.StudentDao;
import ua.com.foxminded.university.data.db.dao.jdbc.mappers.BilateralMapper;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.PropertyReader;

@Repository
public class StudentDaoJdbc implements StudentDao {

    private static final String STUDENTS_SELECT = "students.select";
    private static final String STUDENTS_INSERT = "students.insert";
    private static final String STUDENTS_SELECT_BY_FULL_NAME =
            "students.select.byFullName";
    private static final String STUDENTS_SELECT_BY_ID = "students.select.byId";
    private static final String STUDENTS_UPDATE = "students.update";
    private static final String STUDENTS_DELETE = "students.delete.byId";

    private BilateralMapper<Student> studentMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;
    private PropertyReader queryReader;

    @Autowired
    @Qualifier("queryReader")
    public void setQueryReader(PropertyReader queryReader) {
        this.queryReader = queryReader;
    }

    @Autowired
    public void setStudentMapper(BilateralMapper<Student> studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public Student getById(long id) {
        SqlParameterSource nameParameters = new MapSqlParameterSource("id", id);
        return this.jdbcTemplate.queryForObject(
                queryReader.getProperty(STUDENTS_SELECT_BY_ID),
                nameParameters, studentMapper::mapRow);
    }

    @Override
    public Student getByFullName(String firstName, String lastName) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("firstName", firstName);
        namedParameters.put("lastName", lastName);
        return jdbcTemplate.queryForObject(
                queryReader.getProperty(STUDENTS_SELECT_BY_FULL_NAME),
                namedParameters,
                studentMapper::mapRow);
    }

    @Override
    public List<Student> getAll() {
        return this.jdbcTemplate.query(
                queryReader.getProperty(STUDENTS_SELECT),
                studentMapper::mapRow);
    }

    @Override
    public void save(Student student) {
        Map<String, Object> namedParameters = studentMapper.mapToSave(student);
        this.jdbcTemplate.update(
                queryReader.getProperty(STUDENTS_INSERT),
                namedParameters);
    }

    @Override
    public void save(List<Student> students) {
        List<Map<String, Object>> parametersForSave = students
                .stream()
                .map(studentMapper::mapToSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(parametersForSave);
        this.jdbcTemplate.batchUpdate(
                queryReader.getProperty(STUDENTS_INSERT),
                batch);
    }

    @Override
    public void update(Student student) {
        Map<String, Object> namedParameters = studentMapper.mapToUpdate(
                student);
        this.jdbcTemplate.update(
                queryReader.getProperty(STUDENTS_UPDATE),
                namedParameters);
    }

    @Override
    public void delete(long id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource("id", id);
        this.jdbcTemplate.update(queryReader.getProperty(STUDENTS_DELETE), parameters);
    }

}
