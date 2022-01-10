package ua.com.foxminded.university.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import io.swagger.v3.oas.annotations.Hidden;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.StudentService;
import ua.com.foxminded.university.web.dto.GroupDto;
import ua.com.foxminded.university.web.dto.StudentDto;
import ua.com.foxminded.university.web.mapper.GroupMapper;
import ua.com.foxminded.university.web.mapper.StudentMapper;

@Hidden
@Controller
@RequestMapping("/students")
public class StudentsController {

    private static final String REDIRECT_TO_STUDENTS = "redirect:/students";

    private StudentService studentService;
    private GroupService groupService;
    private StudentMapper studentMapper;
    private GroupMapper groupMapper;

    @Autowired
    protected void setGroupMapper(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Autowired
    public void setStudentMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping()
    public String showStudents(Model model) {
        List<StudentDto> studentsDto = studentService.findAll()
                .stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("students", studentsDto);

        return "/students/list";
    }

    @GetMapping("/new")
    public String showCreatingNew(@ModelAttribute(name = "student") StudentDto studentDto, Model model) {
        List<GroupDto> groupsDto = groupService.findAll()
                .stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("allGroups", groupsDto);
        return "/students/card";
    }

    @GetMapping("/{id}/edit")
    public String showEditCard(Model model, @PathVariable("id") long id) {
        Student student = studentService.findById(id);
        StudentDto studentDto = studentMapper.toDto(student);

        List<GroupDto> groupsDto = groupService.findAll()
                .stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("student", studentDto);
        model.addAttribute("allGroups", groupsDto);
        return "/students/card";
    }

    @PostMapping
    public String create(@ModelAttribute(name = "student") @Valid StudentDto studentDto,
            @RequestParam("groupId") long groupId) {
        Group group = groupService.findById(groupId);
        Student student = studentMapper.toEntity(studentDto, group);
        studentService.save(student);
        return REDIRECT_TO_STUDENTS;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute(name = "student") @Valid StudentDto studentDto,
            @PathVariable("id") long id,
            @RequestParam("groupId") long groupId) {
        Group group = groupService.findById(groupId);
        studentDto.setId(id);
        Student student = studentMapper.toEntity(studentDto, group);
        studentService.save(student);
        return REDIRECT_TO_STUDENTS;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        studentService.deleteById(id);
        return REDIRECT_TO_STUDENTS;
    }

}
