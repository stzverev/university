package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public interface GenericMapper<T> extends RowMapper<T> {

    Map<String, Object> mapToQuery(T row);

}
