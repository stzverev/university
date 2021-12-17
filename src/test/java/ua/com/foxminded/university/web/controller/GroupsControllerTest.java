package ua.com.foxminded.university.web.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.StudentService;
import ua.com.foxminded.university.data.service.TabletimeService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundException;
import ua.com.foxminded.university.web.dto.CourseDto;
import ua.com.foxminded.university.web.dto.GroupDto;
import ua.com.foxminded.university.web.exceptions.RestResponseEntityExceptionHandler;
import ua.com.foxminded.university.web.mapper.CourseMapper;
import ua.com.foxminded.university.web.mapper.GroupMapper;
import ua.com.foxminded.university.web.mapper.StudentMapper;
import ua.com.foxminded.university.web.mapper.TabletimeMapper;
import ua.com.foxminded.university.web.mapper.TeacherMapper;

@ExtendWith(MockitoExtension.class)
class GroupsControllerTest {

    private static final String COURSE_NAME = "test course";

    private static final String GROUP_NAME = "test";

    @Mock
    private GroupService groupService;

    @Mock
    private CourseService courseService;

    @Mock
    private TabletimeService tabletimeService;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private GroupMapper groupMapper;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private TabletimeMapper tabletimeMapper;

    @Spy
    private RestResponseEntityExceptionHandler controllerAdvice =
        new RestResponseEntityExceptionHandler();

    @InjectMocks
    private GroupsController groupController;

    private MockMvc mockMvc;

    private final static long GROUP_ID = 1L;
    private static final long COURSE_ID = 1L;
    private static final long TEACHER_TEST_ID = 1L;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController)
                .setControllerAdvice(controllerAdvice)
                .build();
    }

    @Test
    void shouldGetGroupsListWhenGetGroups() throws Exception {
        mockMvc.perform(get("/groups"))
            .andExpect(status().isOk());
        verify(groupService).findAll();
    }

    @Test
    void shouldGetByIdWhenGetWithId() throws Exception {
        Group testGroup = new Group(GROUP_NAME);
        Set<Student> students = new HashSet<>();
        when(groupService.findById(GROUP_ID)).thenReturn(testGroup);
        when(groupService.findStudents(testGroup)).thenReturn(students);
        mockMvc.perform(get("/groups/" + GROUP_ID + "/edit"))
            .andExpect(status().isOk());
        verify(groupService).findById(GROUP_ID);
    }

    @Test
    void shouldShowCreatingNew() throws Exception {
        mockMvc.perform(get("/groups/new"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldHandleEmptyDataAccesExceptionWhenGroupNotExist() throws Exception {
        ObjectNotFoundException ex = new ObjectNotFoundException(GROUP_ID, Group.class);
        when(groupService.findById(GROUP_ID))
            .thenThrow(ex);
        mockMvc.perform(get("/groups/" + GROUP_ID + "/edit"))
            .andExpect(status().isNotFound());
        verify(controllerAdvice).handleObjectNotFoundById(Mockito.eq(ex), Mockito.any());
    }

    @Test
    void shouldCreateNewGroup() throws Exception {
        Group group = buildTestGroup();
        GroupDto groupDto = buildTestGroupDto();
        when(groupMapper.toEntity(groupDto)).thenReturn(group);

        RequestBuilder request = post("/groups")
                .flashAttr("group", groupDto);
        mockMvc.perform(request)
            .andExpect(status().is3xxRedirection());
        verify(groupService).save(group);
    }

    @Test
    void shouldUpdateGroup() throws Exception {
        Group group = buildTestGroup();
        GroupDto groupDto = buildTestGroupDto();
        when(groupMapper.toEntity(groupDto)).thenReturn(group);

        RequestBuilder request = patch("/groups/" + GROUP_ID)
                .flashAttr("group", groupDto);
        mockMvc.perform(request)
            .andExpect(status().is3xxRedirection());
        verify(groupService).save(group);
    }

    private GroupDto buildTestGroupDto() {
        GroupDto groupDto = new GroupDto();
        groupDto.setId(GROUP_ID);
        groupDto.setName(GROUP_NAME);
        return groupDto;
    }

    private Group buildTestGroup() {
        Group group = new Group(GROUP_NAME);
        group.setId(GROUP_ID);
        return group;
    }

    @Test
    void shouldDeleteGroup() throws Exception {
        Group testGroup = buildTestGroup();
        RequestBuilder request = delete("/groups/" + GROUP_ID)
                .flashAttr("group", testGroup);
        mockMvc.perform(request)
            .andExpect(status().is3xxRedirection());
        verify(groupService).deleteById(GROUP_ID);
    }

    @Test
    void shouldShowAddingACourse() throws Exception {
        Group testGroup = new Group(GROUP_NAME);
        when(groupService.findById(GROUP_ID)).thenReturn(testGroup);
        when(courseService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/groups/" + GROUP_ID + "/add-course"))
            .andExpect(status().isOk());
        verify(groupService).findById(GROUP_ID);
        verify(courseService).findAll();
    }

    @Test
    void shouldShowDeletingACourse() throws Exception {
        Group group = buildTestGroup();
        when(groupService.findWithCoursesById(GROUP_ID)).thenReturn(group);
        when(groupService.findCourses(group)).thenReturn(new HashSet<>());

        mockMvc.perform(get("/groups/" + GROUP_ID + "/delete-course"))
            .andExpect(status().isOk());
        verify(groupService).findWithCoursesById(GROUP_ID);
        verify(groupService).findCourses(group);
    }

    @Test
    void shouldDeleteACourse() throws Exception {
        Group testGroup = buildTestGroup();

        Course testCourse = new Course();
        testCourse.setId(COURSE_ID);
        testCourse.setName(GROUP_NAME);

        when(groupService.findById(testGroup.getId())).thenReturn(testGroup);
        when(courseService.findById(testCourse.getId())).thenReturn(testCourse);

        mockMvc.perform(delete("/groups/delete-course")
                .param("groupId", "" + testGroup.getId())
                .param("courseId", "" + testCourse.getId()))
            .andExpect(status().is3xxRedirection());
        verify(groupService).removeFromCourse(testGroup, testCourse);
    }

    @Test
    void shouldAddCourses() throws Exception {
        Group testGroup = buildTestGroup();

        Course testCourse = new Course();
        testCourse.setId(COURSE_ID);
        testCourse.setName(GROUP_NAME);

        when(groupService.findById(testGroup.getId())).thenReturn(testGroup);
        when(courseService.findById(testCourse.getId())).thenReturn(testCourse);

        mockMvc.perform(post("/groups/add-course")
                .param("groupId", "" + testGroup.getId())
                .param("courseId", "" + testCourse.getId()))
            .andExpect(status().is3xxRedirection());
        verify(groupService).addToCourse(Mockito.any(), Mockito.any());
    }

    @Test
    void shouldShowTabletime() throws Exception {
        Group group = new Group();
        group.setName("test group");
        group.setId(GROUP_ID);

        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        TabletimeRow tabletimeRow = new TabletimeRow(end, new Course(), group, new Teacher());

        when(groupService.findById(group.getId())).thenReturn(group);
        when(tabletimeService.getTabletimeForGroup(group.getId(), begin, end))
            .thenReturn(Collections.singletonList(tabletimeRow));

        mockMvc.perform(get("/groups/" + group.getId() + "/tabletime")
                .param("begin", begin.toString())
                .param("end", end.toString()))
            .andExpect(status().isOk());
    }

    @Test
    void shouldAddingNewRecordToTabletime() throws Exception {
        Course course = buildTestCourse();
        Group group = buildTestGroup();
        group.setCourses(Collections.singleton(course));
        GroupDto groupDto = buildTestGroupDto();
        CourseDto courseDto = buildTestCourseDto();

        when(groupMapper.toDto(group)).thenReturn(groupDto);
        when(courseMapper.toDto(course)).thenReturn(courseDto);
        when(groupService.findWithCoursesById(GROUP_ID)).thenReturn(group);

        mockMvc.perform(get("/groups/" + GROUP_ID + "/tabletime/new"))
            .andExpect(status().isOk());
    }

    private CourseDto buildTestCourseDto() {
        CourseDto courseDto = new CourseDto();
        courseDto.setName(COURSE_NAME);
        courseDto.setId(COURSE_ID);
        return courseDto;
    }

    private Course buildTestCourse() {
        Course course = new Course();
        course.setName(COURSE_NAME);
        course.setId(COURSE_ID);
        return course;
    }

    @Test
    void shouldCreateNewRecordToTabletime() throws Exception {
        Group group = new Group();
        group.setName("test group");
        group.setId(GROUP_ID);

        Course course = new Course();
        course.setName(COURSE_NAME);
        course.setId(COURSE_ID);

        Teacher teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.setId(TEACHER_TEST_ID);

        when(groupService.findById(group.getId())).thenReturn(group);
        when(courseService.findById(course.getId())).thenReturn(course);
        when(teacherService.findById(teacher.getId())).thenReturn(teacher);

        LocalDateTime begin = LocalDateTime.now();
        mockMvc.perform(post("/groups/" + group.getId() + "/tabletime")
                .param("courseId", "" + course.getId())
                .param("teacherId", "" + teacher.getId())
                .param("begin", begin.toString()))
            .andExpect(status().is3xxRedirection());

        TabletimeRow tabletimeRow = new TabletimeRow(begin, course, group, teacher);
        verify(tabletimeService).save(Mockito.eq(tabletimeRow));
    }

}
