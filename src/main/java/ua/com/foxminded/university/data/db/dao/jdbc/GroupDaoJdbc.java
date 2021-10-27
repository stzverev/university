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
import ua.com.foxminded.university.data.db.dao.jdbc.mappers.BilateralMapper;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.model.TabletimeRow;

@Repository
public class GroupDaoJdbc implements GroupDao {

    private static String GROUPS_SELECT;
    private static String GROUPS_INSERT;
    private static String GROUPS_SELECT_BY_ID;
    private static String GROUPS_SELECT_BY_NAME;
    private static String STUDENTS_SELECT_BY_GROUPID;
    private static String GROUPS_UPDATE;
    private static String TABLETIME_INSERT;
    private static String TABLETIME_FOR_GROUP;
    private static String TABLETIME_UPDATE;
    private static String GROUPS_COURSES_SELECT_BY_GROUPID;
    private static String GROUPS_COURSES_INSERT;
    private static String GROUPS_COURSES_DELETE;

    private RowMapper<Group> groupMapper;
    private RowMapper<Student> studentMapper;
    private RowMapper<Course> courseMapper;
    private BilateralMapper<TabletimeRow> tabletimeRowMapper;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${groups.select}")
    public void setGroupsSelect(String groupsSelect) {
        GroupDaoJdbc.GROUPS_SELECT = groupsSelect;
    }

    @Value("${groups.insert}")
    public void setGroupsInsert(String groupsInsert) {
        GroupDaoJdbc.GROUPS_INSERT = groupsInsert;
    }

    @Value("${groups.select}  WHERE groups.id = :id")
    public void setGroupsGetById(String groupsGetById) {
        GroupDaoJdbc.GROUPS_SELECT_BY_ID = groupsGetById;
    }

    @Value("${groups.select}  WHERE groups.name = :name")
    public void setGroupsGetByName(String groupsGetByName) {
        GroupDaoJdbc.GROUPS_SELECT_BY_NAME = groupsGetByName;
    }

    @Value("${students.select} WHERE groups.id = :groupId")
    public void setStudentsGetByGroupId(String studentsGetByGroupId) {
        GroupDaoJdbc.STUDENTS_SELECT_BY_GROUPID = studentsGetByGroupId;
    }

    @Value("${groups.update}")
    public void setGroupsUpdate(String groupsUpdate) {
        GroupDaoJdbc.GROUPS_UPDATE = groupsUpdate;
    }

    @Value("${tabletime.insert}")
    public void setTabletimeInsert(String tabletimeInsert) {
        GroupDaoJdbc.TABLETIME_INSERT = tabletimeInsert;
    }

    @Value(""
            + "${tabletime.select} WHERE"
            + " tabletime.date_time BETWEEN :begin AND :end"
            + " AND tabletime.group_id = :groupId")
    public void setGetTabletimeForGroup(String getTabletimeForGroup) {
        GroupDaoJdbc.TABLETIME_FOR_GROUP = getTabletimeForGroup;
    }

    @Value("${tabletime.update} WHERE group_id = :groupId")
    public void setTabletimeUpdate(String tabletimeUpdate) {
        GroupDaoJdbc.TABLETIME_UPDATE = tabletimeUpdate;
    }

    @Value("${groups_courses.select} WHERE group_id = :id")
    public void setGroupsCoursesSelectByGroupId(
            String groupsCoursesSelectByGroupId) {
        GroupDaoJdbc.GROUPS_COURSES_SELECT_BY_GROUPID =
                groupsCoursesSelectByGroupId;
    }

    @Value("${groups_courses.insert}")
    public void setGroupsCoursesInsert(String groupsCoursesInsert) {
        GroupDaoJdbc.GROUPS_COURSES_INSERT = groupsCoursesInsert;
    }

    @Value(""
            + "${groups_courses.delete}"
            + " WHERE group_id = :groupId"
            + " AND course_id = :courseId")
    public void setGroupsCoursesDelete(String groupsCoursesDelete) {
        GROUPS_COURSES_DELETE = groupsCoursesDelete;
    }

    @Autowired
    public void setGroupMapper(RowMapper<Group> groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Autowired
    public void setStudentMapper(RowMapper<Student> studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Autowired
    public void setTabletimeRowMapper(
            BilateralMapper<TabletimeRow> tabletimeRowMapper) {
        this.tabletimeRowMapper = tabletimeRowMapper;
    }

    @Autowired
    public void setCourseMapper(RowMapper<Course> courseMapper) {
        this.courseMapper = courseMapper;
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
                GROUPS_SELECT_BY_ID, nameParameters, groupMapper::mapRow);
    }

    @Override
    public Group getByName(String name) {
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "name", name);
        return this.jdbcTemplate.queryForObject(
                GROUPS_SELECT_BY_NAME, nameParameters, groupMapper::mapRow);
    }

    @Override
    public List<Group> getAll() {
        return this.jdbcTemplate.query(GROUPS_SELECT,
                groupMapper::mapRow);
    }

    @Override
    public void save(Group group) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                group);
        this.jdbcTemplate.update(GROUPS_INSERT, namedParameters);
    }

    @Override
    public void save(List<Group> groups) {
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(groups);
        this.jdbcTemplate.batchUpdate(GROUPS_INSERT, batch);
    }

    @Override
    public List<Student> getStudents(Group group) {
        SqlParameterSource nameParameters = new MapSqlParameterSource(
                "groupId", group.getId());
        return jdbcTemplate.query(STUDENTS_SELECT_BY_GROUPID,
                nameParameters, studentMapper::mapRow);
    }

    @Override
    public void update(Group group) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(
                group);
        this.jdbcTemplate.update(GROUPS_UPDATE, namedParameters);
    }

    @Override
    public List<TabletimeRow> getTabletime(Group group, LocalDateTime begin,
            LocalDateTime end) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("groupId", group.getId());
        return jdbcTemplate.query(
                TABLETIME_FOR_GROUP, parameters,
                tabletimeRowMapper::mapRow);
    }

    @Override
    public void addTabletimeRows(List<TabletimeRow> rows) {
        List<Map<String, Object>> mapRows = rows
                .stream()
                .map(tabletimeRowMapper::mapToSave)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(
                mapRows);
        jdbcTemplate.batchUpdate(TABLETIME_INSERT, batch);

    }

    @Override
    public void updateTabletime(List<TabletimeRow> rows) {
        List<Map<String, Object>> mapRows = rows
                .stream()
                .map(tabletimeRowMapper::mapToUpdate)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(
                mapRows);
        jdbcTemplate.batchUpdate(TABLETIME_UPDATE, batch);
    }

    @Override
    public List<Course> getCourses(Group group) {
        MapSqlParameterSource parameters = new MapSqlParameterSource(
                "id", group.getId());
        return jdbcTemplate.query(GROUPS_COURSES_SELECT_BY_GROUPID,
                parameters, courseMapper);
    }

    @Override
    public void addToCourses(Group group) {
        List<Map<String, Long>> parameters = group.getCourses()
                .stream()
                .map(it -> {
                    Map<String, Long> map = new HashMap<>();
                    map.put("groupId", group.getId());
                    map.put("courseId", it.getId());
                    return map;
                })
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils
                .createBatch(parameters);
        jdbcTemplate.batchUpdate(GROUPS_COURSES_INSERT, batch);
    }

    @Override
    public void deleteFromCourse(Group group, Course course) {
        Map<String, Long> parameters = new HashMap<>();
        parameters.put("groupId", group.getId());
        parameters.put("courseId", course.getId());
        jdbcTemplate.update(GROUPS_COURSES_DELETE, parameters);
    }

}
