package ua.com.foxminded.university.data.service;

import java.time.LocalDateTime;
import java.util.List;

import ua.com.foxminded.university.data.model.TabletimeRow;

public interface TabletimeService extends CommonService<TabletimeRow> {

    List<TabletimeRow> getTabletimeForCourse(long courseId, LocalDateTime begin,
            LocalDateTime end);

    List<TabletimeRow> getTabletimeForTeacher(long teacherId, LocalDateTime begin,
            LocalDateTime end);

    List<TabletimeRow> getTabletimeForGroup(long groupId, LocalDateTime begin,
            LocalDateTime end);

}
