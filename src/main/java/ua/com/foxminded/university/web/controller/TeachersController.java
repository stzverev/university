package ua.com.foxminded.university.web.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.web.dto.TeacherDto;
import ua.com.foxminded.university.web.mapper.TeacherMapper;

@Controller
@RequestMapping("/teachers")
@RequiredArgsConstructor
public class TeachersController {

    private final TeacherService teacherService;
    private final CourseService courseService;
    private final TeacherMapper teacherMapper;
    private static final String REDIRECT_TO_TEACHERS = "redirect:/teachers";

    @GetMapping()
    public String showTeachers(Model model) {
        model.addAttribute("teachers", findAllTeachersWithCoursesAsDto());
        return "/teachers/list";
    }

    @GetMapping("/new")
    public String showCreatingNew(@ModelAttribute(name = "teacher") TeacherDto teacherDto, Model model) {
        return "/teachers/card";
    }

    @GetMapping("/{id}/edit")
    public String showEditCard(Model model, @PathVariable("id") long id) {
        Teacher teacher = teacherService.findById(id);
        Set<Course> courses = teacherService.getCourses(teacher);
        model.addAttribute("teacher", teacher);
        model.addAttribute("courses", courses);
        return "/teachers/card";
    }

    @PostMapping
    public String create(@ModelAttribute(name = "teacher")  @Valid TeacherDto teacherDto) {
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        teacherService.save(teacher);
        return REDIRECT_TO_TEACHERS;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute(name = "teacher") @Valid TeacherDto teacherDto,
            @PathVariable("id") long id) {
        teacherDto.setId(id);
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        teacherService.save(teacher);
        return REDIRECT_TO_TEACHERS;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        teacherService.deleteById(id);
        return REDIRECT_TO_TEACHERS;
    }

    @GetMapping("/{teacherId}/add-course")
    public String showAddingCourse(@PathVariable("teacherId") long teacherId, Model model) {
        Teacher teacher = teacherService.findById(teacherId);
        List<Course> courses = courseService.findAll();
        model.addAttribute("teacher", teacher);
        model.addAttribute("allCourses", courses);
        return "teachers/add-course";
    }

    @GetMapping("/{teacherId}/delete-course")
    public String showDeletingCourse(@PathVariable("teacherId") long teacherId, Model model) {
        Teacher teacher = teacherService.findById(teacherId);
        teacher.setCourses(teacherService.getCourses(teacher));
        model.addAttribute("teacher", teacher);
        return "teachers/delete-course";
    }

    @DeleteMapping("/delete-course")
    public String deleteCourse(@RequestParam("teacherId") long teacherId, @RequestParam("courseId") long courseId) {
        Teacher teacher = teacherService.findById(teacherId);
        Course course = courseService.findById(courseId);
        teacherService.removeCourse(teacher, course);
        return REDIRECT_TO_TEACHERS;
    }

    @PostMapping("/add-course")
    public String addCourse(@RequestParam("teacherId") long teacherId, @RequestParam("courseId") long courseId) {
        Teacher teacher = teacherService.findById(teacherId);
        Course course = courseService.findById(courseId);
        teacherService.addToCourse(teacher, course);
        return REDIRECT_TO_TEACHERS;
    }

    private List<TeacherDto> findAllTeachersWithCoursesAsDto() {
        List<Teacher> teachers = teacherService.findAll();
        teachers.stream()
            .forEach(teacher -> teacher.setCourses(
                    teacherService.getCourses(teacher)));
        return teachers.stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }

}
