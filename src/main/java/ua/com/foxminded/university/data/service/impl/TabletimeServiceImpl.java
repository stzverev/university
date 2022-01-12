package ua.com.foxminded.university.data.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.data.db.repository.TabletimeRepository;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.service.TabletimeService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class TabletimeServiceImpl implements TabletimeService {

    private static final Class<TabletimeRow> ENTITY_CLASS = TabletimeRow.class;
    private final TabletimeRepository tabletimeRepository;

    @Override
    public List<TabletimeRow> getTabletimeForCourse(long id, LocalDateTime begin, LocalDateTime end) {
        return tabletimeRepository.findByCourseIdAndDateTimeBetween(id, begin, end);
    }

    @Override
    public List<TabletimeRow> getTabletimeForTeacher(long id, LocalDateTime begin, LocalDateTime end) {
        return tabletimeRepository.findByTeacherIdAndDateTimeBetween(id, begin, end);
    }

    @Override
    public List<TabletimeRow> getTabletimeForGroup(long id, LocalDateTime begin, LocalDateTime end) {
        return tabletimeRepository.findByGroupIdAndDateTimeBetween(id, begin, end);
    }

    @Override
    public void save(TabletimeRow tabletime) {
        tabletimeRepository.save(tabletime);
    }

    @Override
    public void save(List<TabletimeRow> tabletimeRows) {
        tabletimeRepository.saveAll(tabletimeRows);
    }

    @Override
    public void deleteById(long id) {
        tabletimeRepository.deleteById(id);
    }

    @Override
    public List<TabletimeRow> findAll() {
        return tabletimeRepository.findAll();
    }

    @Override
    public TabletimeRow findById(long id) {
        return tabletimeRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, ENTITY_CLASS));
    }

    @Override
    public Page<TabletimeRow> findAll(PageRequest pageRequest) {
        return tabletimeRepository.findAll(pageRequest);
    }

}
