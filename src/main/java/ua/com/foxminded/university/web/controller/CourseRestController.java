package ua.com.foxminded.university.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.web.dto.CourseDto;
import ua.com.foxminded.university.web.mapper.CourseMapper;

@RestController
@RequestMapping("/courses-rest")
public class CourseRestController {

    private CourseService courseService;
    private CourseMapper courseMapper;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    public void setCourseMapper(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @GetMapping
    public List<CourseDto> getCourses() {
        return courseService.findAll()
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CourseDto getCourse(@PathVariable long id) {
        Course course = courseService.findById(id);
        return courseMapper.toDto(course);
    }

    @PatchMapping
    public void update(@RequestBody @Valid CourseDto courseDto) {
        Course course = courseMapper.toEntity(courseDto);
        courseService.save(course);
    }

    @PostMapping
    public void create(@RequestBody @Valid CourseDto course) {
        update(course);
    }

    @DeleteMapping
    public void delete(@RequestParam long id) {
        courseService.deleteById(id);
    }

}
