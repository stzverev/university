package ua.com.foxminded.university.data.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.TabletimeDao;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.service.TabletimeService;

@Service
@Transactional
public class TabletimeServiceImpl implements TabletimeService {

    private TabletimeDao tabletimeDao;

    @Autowired
    public TabletimeServiceImpl(TabletimeDao tabletimeDao) {
        super();
        this.tabletimeDao = tabletimeDao;
    }

    @Override
    public List<TabletimeRow> getTabletimeForCourse(long id, LocalDateTime begin, LocalDateTime end) {
        return tabletimeDao.findByCourseIdAndDateTimeBetween(id, begin, end);
    }

    @Override
    public List<TabletimeRow> getTabletimeForTeacher(long id, LocalDateTime begin, LocalDateTime end) {
        return tabletimeDao.findByTeacherIdAndDateTimeBetween(id, begin, end);
    }

    @Override
    public List<TabletimeRow> getTabletimeForGroup(long id, LocalDateTime begin, LocalDateTime end) {
        return tabletimeDao.findByGroupIdAndDateTimeBetween(id, begin, end);
    }

    @Override
    public void save(TabletimeRow tabletime) {
        tabletimeDao.save(tabletime);
    }

    @Override
    public void save(List<TabletimeRow> tabletimeRows) {
        tabletimeDao.saveAll(tabletimeRows);
    }

    @Override
    public void delete(long id) {
        tabletimeDao.deleteById(id);
    }

    @Override
    public List<TabletimeRow> getAll() {
        return tabletimeDao.findAll();
    }

    @Override
    public TabletimeRow getById(long id) {
        return tabletimeDao.getById(id);
    }

}
