package ua.com.foxminded.university.data.db.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.data.model.TabletimeRow;

public interface TabletimeDao extends JpaRepository<TabletimeRow, Long>  {

    List<TabletimeRow> findByGroupIdAndDateTimeBetween(long id, LocalDateTime begin, LocalDateTime end);

    List<TabletimeRow> findByCourseIdAndDateTimeBetween(long id, LocalDateTime begin, LocalDateTime end);

    List<TabletimeRow> findByTeacherIdAndDateTimeBetween(long id, LocalDateTime begin, LocalDateTime end);

}
