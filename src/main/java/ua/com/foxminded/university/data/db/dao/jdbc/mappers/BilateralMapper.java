package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public interface BilateralMapper<T> extends RowMapper<T> {

    Map<String, Object> mapToSave(T object);

    Map<String, Object> mapToUpdate(T object);

}
