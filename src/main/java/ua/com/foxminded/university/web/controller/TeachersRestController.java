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
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.web.dto.TeacherDto;
import ua.com.foxminded.university.web.mapper.TeacherMapper;

@RestController
@RequestMapping("/teachers-rest")
@Tag(name = "Teachers REST controller", description = "This conroller for managing teachers")
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
    @Operation(description = "Returns teachers depending on parameters 'offset' and 'limit'.")
    public List<TeacherDto> findTeachers(@RequestParam int offset, @RequestParam int limit) {
        Sort sortByFullName = Sort.by("firstName")
                .descending()
                .and(Sort.by("lastName")
                        .descending());
        PageRequest pageRequest = PageRequest.of(offset, limit, sortByFullName);
        return teacherService.findAll(pageRequest)
                .stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(description = "Return a teacher by id.")
    public TeacherDto getTeacher(@PathVariable long id) {
        Teacher teacher = teacherService.findById(id);
        return teacherMapper.toDto(teacher);
    }

    @PatchMapping
    @Operation(description = "Update a teacher. Teacher id must not be empty.")
    public void update(@RequestBody @Valid TeacherDto teacherDto) {
        teacherService.save(teacherMapper.toEntity(teacherDto));
    }

    @PostMapping
    @Operation(description = "Create new teacher.")
    public void create(@RequestBody @Valid TeacherDto teacherDto) {
        update(teacherDto);
    }

    @DeleteMapping
    @Operation(description = "Delete a teacher by id.")
    public void delete(@RequestParam(name = "id") long id) {
        teacherService.deleteById(id);
    }

    @PostMapping("/{id}/courses")
    @Operation(description = "Add a course for the teacher.")
    public void addCourse(@PathVariable(name = "id") long teacherId, @RequestParam long courseId) {
        Teacher teacher = teacherService.findById(teacherId);
        Course course = courseService.findById(courseId);
        teacherService.addToCourse(teacher, course);
    }

    @DeleteMapping("/{id}/courses")
    @Operation(description = "Remove a course from the teacher's course list.")
    public void deleteFromCourse(@PathVariable(name = "id") long teacherId, @RequestParam long courseId) {
        Teacher teacher = teacherService.findById(teacherId);
        Course course = courseService.findById(courseId);
        teacherService.removeCourse(teacher, course);
    }

}
