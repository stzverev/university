package ua.com.foxminded.university.data.service;

import java.time.LocalDateTime;
import java.util.List;

import ua.com.foxminded.university.data.model.TabletimeRow;

public interface TabletimeService<T> {

    List<TabletimeRow> getTabletime(T object, LocalDateTime begin,
            LocalDateTime end);

    void addTabletimeRows(List<TabletimeRow> tabletimeRows);

}
