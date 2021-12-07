package ua.com.foxminded.university.web.controller;

import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeachersController {

    private TeacherService teacherService;
    private CourseService courseService;
    private static final String REDIRECT_TO_TEACHERS = "redirect:/teachers";

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping()
    public String showTeachers(Model model) {
        List<Teacher> teachers = teacherService.getAll();
        teachers.stream().forEach(teacher ->
            teacher.setCourses(teacherService.getCourses(teacher)));
        model.addAttribute("teachers", teachers);
        return "/teachers/list";
    }

    @GetMapping("/new")
    public String showCreatingNew(@ModelAttribute Teacher teacher, Model model) {
        return "/teachers/card";
    }

    @GetMapping("/{id}/edit")
    public String showEditCard(Model model, @PathVariable("id") long id) {
        Teacher teacher = teacherService.getById(id);
        Set<Course> courses = teacherService.getCourses(teacher);
        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", courses);
        return "/teachers/card";
    }

    @PostMapping
    public String create(@ModelAttribute Teacher teacher) {
        teacherService.save(teacher);
        return REDIRECT_TO_TEACHERS;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Teacher teacher, @PathVariable("id") long id) {
        teacher.setId(id);
        teacherService.update(teacher);
        return REDIRECT_TO_TEACHERS;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        teacherService.delete(id);
        return REDIRECT_TO_TEACHERS;
    }

    @GetMapping("/{teacherId}/add-course")
    public String showAddingCourse(@PathVariable("teacherId") long teacherId, Model model) {
        Teacher teacher = teacherService.getById(teacherId);
        List<Course> courses = courseService.getAll();
        model.addAttribute("teacher", teacher);
        model.addAttribute("allCourses", courses);
        return "teachers/add-course";
    }

    @GetMapping("/{teacherId}/delete-course")
    public String showDeletingCourse(@PathVariable("teacherId") long teacherId, Model model) {
        Teacher teacher = teacherService.getById(teacherId);
        teacher.setCourses(teacherService.getCourses(teacher));
        model.addAttribute("teacher", teacher);
        return "teachers/delete-course";
    }

    @DeleteMapping("/delete-course")
    public String deleteCourse(@RequestParam("teacherId") long teacherId, @RequestParam("courseId") long courseId) {
        Teacher teacher = teacherService.getById(teacherId);
        Course course = courseService.getById(courseId);
        teacherService.removeFromCourse(teacher, course);
        return REDIRECT_TO_TEACHERS;
    }

    @PostMapping("/add-course")
    public String addCourse(@RequestParam("teacherId") long teacherId, @RequestParam("courseId") long courseId) {
        Teacher teacher = teacherService.getById(teacherId);
        Course course = courseService.getById(courseId);
        teacherService.addToCourse(teacher, course);
        return REDIRECT_TO_TEACHERS;
    }

}
