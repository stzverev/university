package ua.com.foxminded.university.web.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.TabletimeService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.web.dto.CourseDto;
import ua.com.foxminded.university.web.dto.GroupDto;
import ua.com.foxminded.university.web.dto.StudentDto;
import ua.com.foxminded.university.web.dto.TabletimeDto;
import ua.com.foxminded.university.web.dto.TeacherDto;
import ua.com.foxminded.university.web.mapper.CourseMapper;
import ua.com.foxminded.university.web.mapper.GroupMapper;
import ua.com.foxminded.university.web.mapper.StudentMapper;
import ua.com.foxminded.university.web.mapper.TabletimeMapper;
import ua.com.foxminded.university.web.mapper.TeacherMapper;

@Controller
@RequestMapping("/groups")
public class GroupsController {

    private static final String REDIRECT_TO_GROUPS = "redirect:/groups";
    private GroupService groupService;
    private CourseService courseService;
    private TeacherService teacherService;
    private TabletimeService tabletimeService;
    private GroupMapper groupMapper;
    private StudentMapper studentMapper;
    private CourseMapper courseMapper;
    private TeacherMapper teacherMapper;
    private TabletimeMapper tabletimeMapper;

    @Autowired
    public void setCourseMapper(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Autowired
    public void setTeacherMapper(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    @Autowired
    public void setTabletimeMapper(TabletimeMapper tabletimeMapper) {
        this.tabletimeMapper = tabletimeMapper;
    }

    @Autowired
    public void setGroupMapper(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Autowired
    public void setStudentMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

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
        List<GroupDto> groupsDto = findAllGroupsAsDto();
        model.addAttribute("groups", groupsDto);
        return "groups/list";
    }

    @GetMapping("/new")
    public String showCreatingNew(@ModelAttribute(name = "group") GroupDto groupDto) {
        return "groups/card";
    }

    @GetMapping("/{id}/edit")
    public String showEdit(Model model, @PathVariable("id") long id) {
        Group group = groupService.findById(id);
        GroupDto groupDto = groupMapper.toDto(group);
        List<StudentDto> studentsDto = groupService.findStudents(group)
                .stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());

        List<CourseDto> coursesDto = groupService.findCourses(group).stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("group", groupDto);
        model.addAttribute("students", studentsDto);
        model.addAttribute("courses", coursesDto);
        return "groups/card";
    }

    @GetMapping("/{id}/tabletime")
    public String showTableTime(Model model, @PathVariable("id") long groupId,
            @RequestParam("begin") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime begin,
            @RequestParam("end") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime end)  {
        GroupDto groupDto = findGroupByIdAsDto(groupId);
        List<TabletimeDto> tabletimeDto = tabletimeService.getTabletimeForGroup(groupId, begin, end)
                .stream()
                .map(tabletimeMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("group", groupDto);
        model.addAttribute("begin", begin);
        model.addAttribute("end", end);
        model.addAttribute("tabletime", tabletimeDto);
        return "groups/tabletime";
    }

    @GetMapping("/{id}/tabletime/new")
    public String showAddingNewRecordToTabletime(Model model, @PathVariable("id") long groupId) {
        Group group = groupService.findWithCoursesById(groupId);
        GroupDto groupDto = groupMapper.toDto(group);
        List<GroupDto> allGroupsDto = Collections.singletonList(groupDto);
        List<CourseDto> coursesDto = group.getCourses().stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());

        List<TeacherDto> teachersDto = group.getCourses()
                .stream()
                .flatMap(course -> courseService.getTeachers(course).stream())
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("group", groupDto);
        model.addAttribute("allGroups", allGroupsDto);
        model.addAttribute("allCourses", coursesDto);
        model.addAttribute("allTeachers", teachersDto);

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
        model.addAttribute("group", findGroupByIdAsDto(groupId));
        model.addAttribute("allCourses", findAllCoursesAsDto());
        return "groups/add-course";
    }

    private List<CourseDto> findAllCoursesAsDto() {
        return courseService.findAll().stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

    private GroupDto findGroupByIdAsDto(long groupId) {
        Group group = groupService.findById(groupId);
        return groupMapper.toDto(group);
    }

    @GetMapping("/{groupId}/delete-course")
    public String showDeletingCourse(@PathVariable("groupId") long groupId, Model model) {
        Group group = groupService.findWithCoursesById(groupId);
        model.addAttribute("group", groupMapper.toDto(group));
        model.addAttribute("courses", findCoursesAsDto(group));
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
    public String create(@ModelAttribute(name = "group") GroupDto groupDto) {
        Group group = groupMapper.toEntity(groupDto);
        groupService.save(group);
        return REDIRECT_TO_GROUPS;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute(name = "group") GroupDto groupDto, @PathVariable("id") long id) {
        groupDto.setId(id);
        Group group = groupMapper.toEntity(groupDto);
        groupService.save(group);
        return REDIRECT_TO_GROUPS;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        groupService.deleteById(id);
        return REDIRECT_TO_GROUPS;
    }

    private List<GroupDto> findAllGroupsAsDto() {
        return groupService.findAll()
                .stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<CourseDto> findCoursesAsDto(Group group) {
        return groupService.findCourses(group)
                .stream()
                .map(courseMapper::toDto)
                .collect(Collectors.toList());
    }

}
