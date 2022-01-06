package ua.com.foxminded.university.web.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.service.TabletimeService;
import ua.com.foxminded.university.web.dto.TabletimeDto;
import ua.com.foxminded.university.web.mapper.TabletimeMapper;

@RestController
@RequestMapping("/tabletime-rest")
public class TabletimeRestController {

    private TabletimeService tabletimeService;
    private TabletimeMapper tabletimeMapper;

    @Autowired
    public void setTabletimeMapper(TabletimeMapper tabletimeMapper) {
        this.tabletimeMapper = tabletimeMapper;
    }

    @Autowired
    public void setTabletimeService(TabletimeService tabletimeService) {
        this.tabletimeService = tabletimeService;
    }

    @PostMapping
    public void addTabletimeRow(@RequestBody TabletimeDto tabletimeDto) {
        TabletimeRow tabletime = tabletimeMapper.toEntity(tabletimeDto);
        tabletimeService.save(tabletime);
    }

    @DeleteMapping
    public void delete(@RequestParam long id) {
        tabletimeService.deleteById(id);
    }

    @GetMapping("/teachers")
    public List<TabletimeDto> getForTeacher(@RequestParam long teacherId,
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime begin,
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end) {
        return tabletimeService.getTabletimeForTeacher(teacherId, begin, end)
                .stream()
                .map(tabletimeMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/courses")
    public List<TabletimeDto> getForCourse(@RequestParam long courseId,
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime begin,
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end) {
        return tabletimeService.getTabletimeForCourse(courseId, begin, end)
                .stream()
                .map(tabletimeMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/groups")
    public List<TabletimeDto> getForGroup(@RequestParam long groupId,
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime begin,
            @RequestParam @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end) {
        return tabletimeService.getTabletimeForGroup(groupId, begin, end)
                .stream()
                .map(tabletimeMapper::toDto)
                .collect(Collectors.toList());
    }

}
