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
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private GroupService groupService;
    private CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("")
    public String showGroups(Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "groups/list";
    }

    @GetMapping("/new")
    public String showCreatingNew(@ModelAttribute Group group) {
        return "groups/card";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(Model model, @PathVariable("id") long id) {
        Group group = groupService.getById(id);
        List<Student> students = groupService.getStudents(group);
        group.setCourses(groupService.getCourses(group));
        model.addAttribute("group", group);
        model.addAttribute("students", students);
        return "groups/card";
    }

    @GetMapping("/{groupId}/add-course")
    public String showAddingCourse(@PathVariable("groupId") long groupId, Model model) {
        Group group = groupService.getById(groupId);
        List<Course> courses = courseService.getAll();
        model.addAttribute("group", group);
        model.addAttribute("allCourses", courses);
        return "groups/add-course";
    }

    @GetMapping("/{groupId}/delete-course")
    public String showDeletingCourse(@PathVariable("groupId") long groupId, Model model) {
        Group group = groupService.getById(groupId);
        group.setCourses(groupService.getCourses(group));
        model.addAttribute("group", group);
        return "groups/delete-course";
    }

}
