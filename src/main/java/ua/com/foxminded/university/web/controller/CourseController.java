package ua.com.foxminded.university.web.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.web.dto.CourseDto;
import ua.com.foxminded.university.web.mapper.CourseMapper;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private static final String REDIRECT_TO_COURSES = "redirect:/courses";
    private CourseService courseService;
    private CourseMapper courseMapper;

    @Autowired
    public void setCourseMapper(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping()
    public String showCourses(@ModelAttribute(name = "course") CourseDto courseDto, Model model) {
        model.addAttribute("courses", findAllCoursesAsDto());
        return "courses/list";
    }

    private List<CourseDto> findAllCoursesAsDto() {
        return courseService.findAll()
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/new")
    public String showCreatingNew(@ModelAttribute(name = "course") CourseDto courseDto) {
        return "courses/card";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(Model model, @PathVariable("id") long id) {
        Course course = courseService.findById(id);
        Set<Group> groups = courseService.getGroups(course);
        course.setGroups(groups);
        CourseDto courseDto = courseMapper.toDto(course);
        model.addAttribute("course", courseDto);
        return "courses/card";
    }

    @PostMapping()
    public String create(@ModelAttribute(name = "course") CourseDto courseDto) {
        courseService.save(courseMapper.toEntity(courseDto));
        return REDIRECT_TO_COURSES;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute(name = "course") CourseDto courseDto,
            @PathVariable("id") long id) {
        courseDto.setId(id);
        courseService.save(courseMapper.toEntity(courseDto));
        return REDIRECT_TO_COURSES;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        courseService.deleteById(id);
        return REDIRECT_TO_COURSES;
    }

}
