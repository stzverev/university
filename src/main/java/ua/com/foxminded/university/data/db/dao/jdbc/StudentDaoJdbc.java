package ua.com.foxminded.university.data.db.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.db.dao.StudentDao;
import ua.com.foxminded.university.data.db.dao.jdbc.mappers.BilateralMapper;
import ua.com.foxminded.university.data.model.Student;

@Repository
public class StudentDaoJdbc implements StudentDao {

    private static String STUDENTS_SELECT;
    private static String STUDENTS_INSERT;
    private static String STUDENTS_SELECT_BY_FULL_NAME;
    private static String STUDENTS_SELECT_BY_ID;
    private static String STUDENTS_UPDATE;

    private BilateralMapper<Student> studentMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${students.select}")
    public void setStudentsSelect(String studentsSelect) {
        StudentDaoJdbc.STUDENTS_SELECT = studentsSelect;
    }

    @Value("${students.insert}")
    public void setStudentsInsert(String studentsInsert) {
        StudentDaoJdbc.STUDENTS_INSERT = studentsInsert;
    }

    @Value(""
            + "${students.select}"
            + " WHERE students.first_Name = :firstName"
            + " AND students.last_name = :lastName")
    public void setStudentsGetByFullNamel(String studentsGetByFullNamel) {
        StudentDaoJdbc.STUDENTS_SELECT_BY_FULL_NAME = studentsGetByFullNamel;
    }

    @Value("${students.select} WHERE students.id = :id")
    public void setStudentsGetById(String studentsGetById) {
        StudentDaoJdbc.STUDENTS_SELECT_BY_ID = studentsGetById;
    }

    @Value("${students.update}")
    public void setStudentsUpdate(String studentsUpdate) {
        StudentDaoJdbc.STUDENTS_UPDATE = studentsUpdate;
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
                STUDENTS_SELECT_BY_ID, nameParameters, studentMapper::mapRow);
    }

    @Override
    public Student getByFullName(String firstName, String lastName) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("firstName", firstName);
        namedParameters.put("lastName", lastName);
        return jdbcTemplate.queryForObject(
                STUDENTS_SELECT_BY_FULL_NAME, namedParameters,
                studentMapper::mapRow);
    }

    @Override
    public List<Student> getAll() {
        return this.jdbcTemplate.query(STUDENTS_SELECT,
                studentMapper::mapRow);
    }

    @Override
    public void save(Student student) {
        Map<String, Object> namedParameters = studentMapper.mapToSave(student);
        this.jdbcTemplate.update(STUDENTS_INSERT, namedParameters);
    }

    @Override
    public void save(List<Student> students) {
        List<Map<String, Object>> parametersForSave = students
                .stream()
                .map(studentMapper::mapToSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(parametersForSave);
        this.jdbcTemplate.batchUpdate(STUDENTS_INSERT, batch);
    }

    @Override
    public void update(Student student) {
        Map<String, Object> namedParameters = studentMapper.mapToUpdate(
                student);
        this.jdbcTemplate.update(STUDENTS_UPDATE, namedParameters);
    }

}
