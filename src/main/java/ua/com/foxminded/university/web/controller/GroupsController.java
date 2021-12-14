package ua.com.foxminded.university.web.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.TabletimeService;
import ua.com.foxminded.university.data.service.TeacherService;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private static final String REDIRECT_TO_GROUPS = "redirect:/groups";
    private GroupService groupService;
    private CourseService courseService;
    private TeacherService teacherService;
    private TabletimeService tabletimeService;
    private Logger logger = LoggerFactory.getLogger(GroupsController.class);

    @Autowired
    public void setTabletimeService(TabletimeService tabletimeService) {
        this.tabletimeService = tabletimeService;
    }

    @Autowired
    public void setTeacherService(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping()
    public String showGroups(Model model) {
        model.addAttribute("groups", groupService.findAll());
        return "groups/list";
    }

    @GetMapping("/new")
    public String showCreatingNew(@ModelAttribute Group group) {
        return "groups/card";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(Model model, @PathVariable("id") long id) {
        Group group = groupService.findById(id);
        Set<Student> students = groupService.getStudents(group);
        logger.debug("students: {}", students.size());
        Set<Course> courses = groupService.getCourses(group);
        logger.debug("courses: {}", courses.size());
        model.addAttribute("group", group);
        model.addAttribute("students", students);
        model.addAttribute("courses", courses);
        return "groups/card";
    }

    @GetMapping("/{id}/tabletime")
    public String showTableTime(Model model, @PathVariable("id") long groupId,
            @RequestParam("begin") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime begin,
            @RequestParam("end") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end)  {
        Group group = groupService.findById(groupId);
        List<TabletimeRow> tabletime = tabletimeService.getTabletimeForGroup(groupId, begin, end);
        model.addAttribute("group", group );
        model.addAttribute("begin", begin);
        model.addAttribute("end", end);
        model.addAttribute("tabletime", tabletime);
        return "groups/tabletime";
    }

    @GetMapping("/{id}/tabletime/new")
    public String showAddingNewRecordToTabletime(Model model, @PathVariable("id") long groupId) {
        Group group = groupService.findById(groupId);
        group.setCourses(groupService.getCourses(group));
        List<Group> allGroups = Collections.singletonList(group);
        List<Teacher> teachers = group.getCourses()
                .stream()
                .flatMap(course -> courseService.getTeachers(course).stream())
                .collect(Collectors.toList());
        model.addAttribute("group", group);
        model.addAttribute("allGroups", allGroups);
        model.addAttribute("allCourses", group.getCourses());
        model.addAttribute("allTeachers", teachers);

        return "groups/tabletime-new";
    }

    @PostMapping("/{id}/tabletime")
    public String createNewRecordToTabletime(
            @PathVariable("id") long groupId,
            @RequestParam("courseId") long courseId,
            @RequestParam("teacherId") long teacherId,
            @RequestParam("begin") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime begin) {
        Group group = groupService.findById(groupId);
        Course course = courseService.findById(courseId);
        Teacher teacher = teacherService.findById(teacherId);

        TabletimeRow tableTimeRow = new TabletimeRow(begin, course, group, teacher);
        tabletimeService.save(tableTimeRow);

        return REDIRECT_TO_GROUPS + "/" + groupId + "/edit";
    }

    @GetMapping("/{groupId}/add-course")
    public String showAddingCourse(@PathVariable("groupId") long groupId, Model model) {
        Group group = groupService.findById(groupId);
        List<Course> courses = courseService.findAll();
        model.addAttribute("group", group);
        model.addAttribute("allCourses", courses);
        return "groups/add-course";
    }

    @GetMapping("/{groupId}/delete-course")
    public String showDeletingCourse(@PathVariable("groupId") long groupId, Model model) {
        Group group = groupService.findById(groupId);
        group.setCourses(groupService.getCourses(group));
        model.addAttribute("group", group);
        return "groups/delete-course";
    }

    @DeleteMapping("/delete-course")
    public String deleteCourse(@RequestParam("groupId") long groupId, @RequestParam("courseId") long courseId) {
        Group group = groupService.findById(groupId);
        Course course = courseService.findById(courseId);
        groupService.removeFromCourse(group, course);
        return REDIRECT_TO_GROUPS;
    }

    @PostMapping("/add-course")
    public String addCourse(@RequestParam("groupId") long groupId, @RequestParam("courseId") long courseId) {
        Group group = groupService.findById(groupId);
        Course course = courseService.findById(courseId);
        groupService.addToCourse(group, course);
        return REDIRECT_TO_GROUPS;
    }

    @PostMapping()
    public String create(@ModelAttribute Group group) {
        groupService.save(group);
        return REDIRECT_TO_GROUPS;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Group group, @PathVariable("id") long id) {
        group.setId(id);
        groupService.save(group);
        return REDIRECT_TO_GROUPS;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        groupService.deleteById(id);
        return REDIRECT_TO_GROUPS;
    }

}
