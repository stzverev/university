package ua.com.foxminded.university.data.db.dao.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.db.dao.StudentDao;
import ua.com.foxminded.university.data.model.Student;

@Repository
public class StudentDaoJdbc implements StudentDao {

    private final String studentsSelect;
    private final String studentsInsert;
    private final String studentsGetByFullNamel;
    private final String studentsGetById;

    @Autowired
    private RowMapper<Student> studentMapper;

    private NamedParameterJdbcTemplate jdbcTemplate;

    public StudentDaoJdbc(@Value("${students.select}") String studentsSelect,
            @Value("${students.insert}") String studentsInsert,
            @Value(""
                    + "${students.select}"
                    + " WHERE students.first_Name = :firstName"
                    + " AND students.last_name = :lastName")
            String studentsGetByFullNamel,
            @Value("${students.select} WHERE students.id = :id")
            String studentsGetById) {
        super();
        this.studentsSelect = studentsSelect;
        this.studentsInsert = studentsInsert;
        this.studentsGetByFullNamel = studentsGetByFullNamel;
        this.studentsGetById = studentsGetById;
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
                studentsGetById, nameParameters, studentMapper::mapRow);
    }

    @Override
    public Student getByFullName(String firstName, String lastName) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("firstName", firstName);
        namedParameters.put("lastName", lastName);
        return jdbcTemplate.queryForObject(
                studentsGetByFullNamel, namedParameters, studentMapper::mapRow);
    }

    @Override
    public List<Student> getAll() {
        return this.jdbcTemplate.query(studentsSelect,
                studentMapper::mapRow);
    }

    @Override
    public void save(Student student) {
        Map<String, Object> namedParameters = parametersForSave(student);
        this.jdbcTemplate.update(studentsInsert, namedParameters);
    }

    @Override
    public void save(List<Student> students) {
        List<Map<String, Object>> parametersForSave = students
                .stream()
                .map(this::parametersForSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(parametersForSave);
        this.jdbcTemplate.batchUpdate(studentsInsert, batch);
    }

    private Map<String, Object> parametersForSave(Student student) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("firstName", student.getFirstName());
        namedParameters.put("lastName", student.getLastName());
        namedParameters.put("groupId", student.getGroup().getId());
        return namedParameters;
    }

}
