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
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.web.dto.TeacherDto;
import ua.com.foxminded.university.web.mapper.TeacherMapper;

@RestController
@RequestMapping("/teachers-rest")
public class TeachersRestController {

    private TeacherService teacherService;
    private TeacherMapper teacherMapper;
    private CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Autowired
    public void setTeacherMapper(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    @GetMapping
    public List<TeacherDto> findTeachers() {
        return teacherService.findAll()
                .stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TeacherDto getTeacher(@PathVariable long id) {
        Teacher teacher = teacherService.findById(id);
        return teacherMapper.toDto(teacher);
    }

    @PatchMapping
    public void update(@RequestBody @Valid TeacherDto teacherDto) {
        teacherService.save(teacherMapper.toEntity(teacherDto));
    }

    @PostMapping
    public void create(@RequestBody @Valid TeacherDto teacherDto) {
        update(teacherDto);
    }

    @DeleteMapping
    public void delete(@RequestParam(name = "id") long id) {
        teacherService.deleteById(id);
    }

    @PostMapping("/{id}/courses")
    public void addCourse(@PathVariable(name = "id") long teacherId, @RequestParam long courseId) {
        Teacher teacher = teacherService.findById(teacherId);
        Course course = courseService.findById(courseId);
        teacherService.addToCourse(teacher, course);
    }

    @DeleteMapping("/{id}/courses")
    public void deleteFromCourse(@PathVariable(name = "id") long teacherId, @RequestParam long courseId) {
        Teacher teacher = teacherService.findById(teacherId);
        Course course = courseService.findById(courseId);
        teacherService.removeCourse(teacher, course);
    }

}
