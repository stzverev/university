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
import ua.com.foxminded.university.data.model.Tabletime;

@Repository
public class GroupDaoJdbc implements GroupDao {

    @Value("${groups.select}")
    private String groupsSelect;

    @Value("${groups.insert}")
    private String groupsInsert;

    @Value("${students.select}")
    private String studentsSelect;

    @Autowired
    private RowMapper<Group> groupMapper;

    @Autowired
    private RowMapper<Student> studentMapper;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public Group getById(long id) {
        String sql = groupsSelect + " WHERE groups.id = :id";
        SqlParameterSource nameParameters = new MapSqlParameterSource("id", id);
        return this.jdbcTemplate.queryForObject(
                sql, nameParameters, groupMapper::mapRow);
    }

    @Override
    public Group getByName(String name) {
        String sql = groupsSelect + " WHERE groups.name = :name";
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "name", name);
        return this.jdbcTemplate.queryForObject(
                sql, nameParameters, groupMapper::mapRow);
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
        String sql = studentsSelect + " WHERE groups.id = :groupId";
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "groupId", group.getId());
        return jdbcTemplate.query(sql, nameParameters, studentMapper::mapRow);
    }

    @Override
    public Tabletime getTabletime(Group group) {
        // TODO Auto-generated method stub
        return null;
    }

}
