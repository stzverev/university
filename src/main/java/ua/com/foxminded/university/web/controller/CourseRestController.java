package ua.com.foxminded.university.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.web.dto.CourseDto;
import ua.com.foxminded.university.web.mapper.CourseMapper;

@RestController
@RequestMapping("/courses-rest")
@Tag(name = "Course controller", description = "This conroller for managing courses.")
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
    @Operation(description = "Returns courses depending on parameters 'limit' and 'offset.'")
    public List<CourseDto> getCourses(@RequestParam int offset, @RequestParam int limit) {
        Sort sortByName = Sort.by("name").descending();
        PageRequest pageRequest = PageRequest.of(offset, limit, sortByName);
        return courseService.findAll(pageRequest)
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(description = "Returns a course by id.")
    public CourseDto getCourse(@PathVariable long id) {
        Course course = courseService.findById(id);
        return courseMapper.toDto(course);
    }

    @PatchMapping
    @Operation(description = "Update a course. The course id must not be empty.")
    public void update(@RequestBody @Valid CourseDto courseDto) {
        Course course = courseMapper.toEntity(courseDto);
        courseService.save(course);
    }

    @PostMapping
    @Operation(description = "Create new course.")
    public void create(@RequestBody @Valid CourseDto course) {
        update(course);
    }

    @DeleteMapping
    @Operation(description = "Delete course by id.")
    public void delete(@RequestParam long id) {
        courseService.deleteById(id);
    }

}
