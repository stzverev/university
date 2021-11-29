package ua.com.foxminded.university.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ua.com.foxminded.university.data.service.CourseService;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private static final String REDIRECT_TO_COURSES = "redirect:/courses";
    private final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping()
    public String showCourses(@ModelAttribute Course course, Model model) {
        List<Course> courses = courseService.getAll();
        logger.debug("Getting {} courses", courses.size());
        model.addAttribute("courses", courses);
        return "courses/list";
    }

    @GetMapping("/new")
    public String showCreatingNew(@ModelAttribute Course course) {
        return "courses/card";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(Model model, @PathVariable("id") long id) {
        Course course = courseService.getById(id);
        model.addAttribute("course", course);
        return "courses/card";
    }

    @PostMapping()
    public String create(@ModelAttribute Course course) {
        courseService.save(course);
        return REDIRECT_TO_COURSES;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Course course, @PathVariable("id") long id) {
        course.setId(id);
        courseService.update(course);
        return REDIRECT_TO_COURSES;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        courseService.delete(id);
        return REDIRECT_TO_COURSES;
    }

}
