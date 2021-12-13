package ua.com.foxminded.university.data.service;

import java.time.LocalDateTime;
import java.util.List;

import ua.com.foxminded.university.data.model.TabletimeRow;

public interface TabletimeService extends CommonService<TabletimeRow> {

    List<TabletimeRow> getTabletimeForCourse(long id, LocalDateTime begin,
            LocalDateTime end);

    List<TabletimeRow> getTabletimeForTeacher(long id, LocalDateTime begin,
            LocalDateTime end);

    List<TabletimeRow> getTabletimeForGroup(long id, LocalDateTime begin,
            LocalDateTime end);

}
