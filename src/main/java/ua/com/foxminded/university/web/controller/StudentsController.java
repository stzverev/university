package ua.com.foxminded.university.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
