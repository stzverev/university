package ua.com.foxminded.university.data.db.dao.jdbc;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Tabletime;
import ua.com.foxminded.university.data.model.TabletimeRow;

@Repository
public class CourseTabletimeDaoJdbc extends TabletimeDaoJdbc<Course> {

    @Value("${tabletime.select}")
    private String tabletimeSelect;

    @Autowired
    private RowMapper<TabletimeRow> tabletimeRowMapper;

    @Override
    public Tabletime getTabletime(Course course, LocalDateTime begin,
            LocalDateTime end) {
        String sql = String.format(""
                + "%s%n WHERE %n"
                + "tabletime.date_time BETWEEN :begin AND :end %n"
                + "AND tabletime.course_id = :courseId",
                tabletimeSelect);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("begin", Timestamp.valueOf(begin));
        parameters.put("end", Timestamp.valueOf(end));
        parameters.put("courseId", course.getId());
        List<TabletimeRow> list =
                getJdbcTemplate().query(
                        sql, parameters, tabletimeRowMapper::mapRow);
        return new Tabletime(begin, end, list);
    }

}
