package ua.com.foxminded.university.data.db.dao.jdbc;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.db.dao.TabletimeDao;
import ua.com.foxminded.university.data.model.Tabletime;
import ua.com.foxminded.university.data.model.TabletimeRow;

@Repository
public abstract class TabletimeDaoJdbc<T> implements TabletimeDao<T> {

    @Value("${tabletime.insert}")
    private String tabletimeInsert;


    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(
                dataSource);
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Override
    public void saveTabletime(Tabletime tabletime) {
        List<Map<String, Object>> rows = tabletime.getRows()
                .stream()
                .map(this::mapParam)
                .collect(Collectors.toList());
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(rows);
        jdbcTemplate.batchUpdate(tabletimeInsert, batch);
    }

    private Map<String, Object> mapParam(TabletimeRow row) {
        Map<String, Object> map = new HashMap<>();
        map.put("dateTime", Timestamp.valueOf(row.getDateTime()));
        map.put("groupId", row.getGroup().getId());
        map.put("teacherId", row.getTeacher().getId());
        map.put("courseId", row.getCourse().getId());
        return map;
    }

}
