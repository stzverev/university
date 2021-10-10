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

import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.model.Group;

@Repository
public class GroupDaoJdbc implements GroupDao {

    @Value("${groups.get}")
    private String queryGet;

    @Value("${groups.insert}")
    private String queryInsert;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    @Override
    public Group getById(long id) {
        String sql = queryGet + " WHERE id = :id";
        SqlParameterSource nameParameters = new MapSqlParameterSource("id", id);
        return this.namedParameterJdbcTemplate.queryForObject(
                sql, nameParameters, this::mapGroup);
    }

    @Override
    public Group getByName(String name) {
        String sql = queryGet + " WHERE name = :name";
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "name", name);
        return this.namedParameterJdbcTemplate.queryForObject(
                sql, nameParameters, this::mapGroup);
    }

    @Override
    public List<Group> getAll() {
        return this.namedParameterJdbcTemplate.query(queryGet,
                this::mapGroup);
    }

    @Override
    public void save(Group group) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                group);
        this.namedParameterJdbcTemplate.update(queryInsert, namedParameters);
    }

    @Override
    public void save(List<Group> groups) {
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(groups);
        this.namedParameterJdbcTemplate.batchUpdate(queryInsert, batch);
    }

    private Group mapGroup(ResultSet resultSet, int rowNum)
            throws SQLException {
        Group group = new Group();
        group.setId(resultSet.getLong("id"));
        group.setName(resultSet.getString("name"));
        return group;
    }

}
