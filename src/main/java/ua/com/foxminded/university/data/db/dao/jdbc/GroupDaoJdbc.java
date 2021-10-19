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

import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;

@Repository
public class GroupDaoJdbc implements GroupDao {

    private final String groupsSelect;
    private final String groupsInsert;
    private final String groupsGetById;
    private final String groupsGetByName;
    private final String studentsGetByGroupId;
    private final String groupsUpdate;

    @Autowired
    private RowMapper<Group> groupMapper;

    @Autowired
    private RowMapper<Student> studentMapper;

    private NamedParameterJdbcTemplate jdbcTemplate;

    public GroupDaoJdbc(
            @Value("${groups.select}")
            String groupsSelect,

            @Value("${groups.insert}")
            String groupsInsert,

            @Value("${groups.select}  WHERE groups.id = :id")
            String groupsGetById,

            @Value("${groups.select}  WHERE groups.name = :name")
            String groupsGetByName,

            @Value("${students.select} WHERE groups.id = :groupId")
            String studentsGetByGroupId,

            @Value("${groups.update}")
            String groupsUpdate) {
        super();
        this.groupsSelect = groupsSelect;
        this.groupsInsert = groupsInsert;
        this.groupsGetById = groupsGetById;
        this.groupsGetByName = groupsGetByName;
        this.studentsGetByGroupId = studentsGetByGroupId;
        this.groupsUpdate = groupsUpdate;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public Group getById(long id) {
        SqlParameterSource nameParameters = new MapSqlParameterSource("id", id);
        return this.jdbcTemplate.queryForObject(
                groupsGetById, nameParameters, groupMapper::mapRow);
    }

    @Override
    public Group getByName(String name) {
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "name", name);
        return this.jdbcTemplate.queryForObject(
                groupsGetByName, nameParameters, groupMapper::mapRow);
    }

    @Override
    public List<Group> getAll() {
        return this.jdbcTemplate.query(groupsSelect,
                groupMapper::mapRow);
    }

    @Override
    public void save(Group group) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                group);
        this.jdbcTemplate.update(groupsInsert, namedParameters);
    }

    @Override
    public void save(List<Group> groups) {
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(groups);
        this.jdbcTemplate.batchUpdate(groupsInsert, batch);
    }

    @Override
    public List<Student> getStudents(Group group) {
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "groupId", group.getId());
        return jdbcTemplate.query(studentsGetByGroupId,
                nameParameters, studentMapper::mapRow);
    }

    @Override
    public void update(Group group) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                group);
        this.jdbcTemplate.update(groupsUpdate, namedParameters);
    }

}
