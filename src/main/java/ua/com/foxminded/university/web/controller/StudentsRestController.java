package ua.com.foxminded.university.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.StudentService;
import ua.com.foxminded.university.web.dto.StudentDto;
import ua.com.foxminded.university.web.mapper.StudentMapper;

@RestController
@RequestMapping("/students-rest")
public class StudentsRestController {

    private StudentService studentService;
    private StudentMapper studentMapper;
    private GroupService groupService;

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public void setStudentMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @GetMapping
    public List<StudentDto> findStudents(@RequestParam int limit, @RequestParam int offset) {
        Sort sortByFullName = Sort.by("firstName")
                .descending()
                .and(Sort.by("lastName")
                        .descending());
        PageRequest pageRequest = PageRequest.of(offset, limit, sortByFullName);
        Page<Student> page = studentService.findAll(pageRequest);
        return page.getContent()
                .stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StudentDto getStudent(@PathVariable long id) {
        Student student = studentService.findById(id);
        return studentMapper.toDto(student);
    }

    @PatchMapping
    public void update(@RequestBody @Valid StudentDto studentDto) {
        Group group = groupService.findById(studentDto.getGroupId());
        studentService.save(studentMapper.toEntity(studentDto, group));
    }

    @PostMapping
    public void create(@RequestBody @Valid StudentDto studentDto) {
        update(studentDto);
    }

    @DeleteMapping
    public void delete(@RequestParam(name = "id") long id) {
        studentService.deleteById(id);
    }

}
