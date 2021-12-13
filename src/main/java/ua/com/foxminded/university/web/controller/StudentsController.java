package ua.com.foxminded.university.web.controller;

import java.util.List;

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

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentsController {

    private StudentService studentService;
    private GroupService groupService;
    private static final String REDIRECT_TO_STUDENTS = "redirect:/students";

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
        List<Student> students = studentService.getAll();
        model.addAttribute("students", students);
        return "/students/list";
    }

    @GetMapping("/new")
    public String showCreatingNew(@ModelAttribute Student student, Model model) {
        model.addAttribute("allGroups", groupService.getAll());
        return "/students/card";
    }

    @GetMapping("/{id}/edit")
    public String showEditCard(Model model, @PathVariable("id") long id) {
        Student student = studentService.getById(id);
        model.addAttribute("student", student);
        model.addAttribute("allGroups", groupService.getAll());
        return "/students/card";
    }

    @PostMapping
    public String create(@ModelAttribute Student student, @RequestParam("groupId") long groupId) {
        Group group = groupService.getById(groupId);
        student.setGroup(group);
        studentService.save(student);
        return REDIRECT_TO_STUDENTS;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Student student, @PathVariable("id") long id,
            @RequestParam("groupId") long groupId) {
        student.setId(id);
        Group group = groupService.getById(groupId);
        student.setGroup(group);
        studentService.save(student);
        return REDIRECT_TO_STUDENTS;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        studentService.delete(id);
        return REDIRECT_TO_STUDENTS;
    }

}
