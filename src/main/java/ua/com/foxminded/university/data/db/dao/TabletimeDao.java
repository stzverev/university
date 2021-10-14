package ua.com.foxminded.university.data.db.dao;

import java.time.LocalDateTime;

import ua.com.foxminded.university.data.model.Tabletime;

public interface TabletimeDao<T> {

    void saveTabletime(Tabletime tabletime);

    Tabletime getTabletime(T object, LocalDateTime begin,
            LocalDateTime end);

}
