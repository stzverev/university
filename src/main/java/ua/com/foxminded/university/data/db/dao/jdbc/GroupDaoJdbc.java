package ua.com.foxminded.university.data.db.dao.jdbc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import ua.com.foxminded.university.data.db.dao.jdbc.mappers.GenericMapper;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.model.TabletimeRow;

@Repository
public class GroupDaoJdbc implements GroupDao {

    private final String groupsSelect;
    private final String groupsInsert;
    private final String groupsGetById;
    private final String groupsGetByName;
    private final String studentsGetByGroupId;
    private final String groupsUpdate;
    private final String tabletimeInsert;
    private final String getTabletimeForGroup;
    private final String tabletimeUpdate;

    @Autowired
    private RowMapper<Group> groupMapper;

    @Autowired
    private RowMapper<Student> studentMapper;

    @Autowired
    private GenericMapper<TabletimeRow> tabletimeRowMapper;

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
            String groupsUpdate,

            @Value("${tabletime.insert}")
            String tabletimeInsert,

            @Value(""
                    + "${tabletime.select} WHERE"
                    + " tabletime.date_time BETWEEN :begin AND :end"
                    + " AND tabletime.group_id = :groupId")
            String getTabletimeForGroup,

            @Value("${tabletime.update} WHERE group_id = :groupId")
            String tabletimeUpdate) {
        super();
        this.groupsSelect = groupsSelect;
        this.groupsInsert = groupsInsert;
        this.groupsGetById = groupsGetById;
        this.groupsGetByName = groupsGetByName;
        this.studentsGetByGroupId = studentsGetByGroupId;
        this.groupsUpdate = groupsUpdate;
        this.tabletimeInsert = tabletimeInsert;
        this.tabletimeUpdate = tabletimeUpdate;
        this.getTabletimeForGroup = getTabletimeForGroup;
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

    @Override
    public List<TabletimeRow> getTabletime(Group group, LocalDateTime begin,
            LocalDateTime end) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("groupId", group.getId());
        return jdbcTemplate.query(
                getTabletimeForGroup, parameters,
                tabletimeRowMapper::mapRow);
    }

    @Override
    public void saveTabletime(List<TabletimeRow> rows) {
        List<Map<String, Object>> mapRows = rows
                .stream()
                .map(tabletimeRowMapper::mapToSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(
                mapRows);
        jdbcTemplate.batchUpdate(tabletimeInsert, batch);

    }

    @Override
    public void updateTabletime(List<TabletimeRow> rows) {
        List<Map<String, Object>> mapRows = rows
                .stream()
                .map(tabletimeRowMapper::mapToSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(
                mapRows);
        jdbcTemplate.batchUpdate(tabletimeUpdate, batch);
    }

}
