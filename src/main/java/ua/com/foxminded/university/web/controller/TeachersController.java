package ua.com.foxminded.university.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeachersController {

    private TeacherService teacherService;
    private CourseService courseService;

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
        teacher.setCourses(teacherService.getCourses(teacher));
        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", teacher.getCourses());
        return "/teachers/card";
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

}
