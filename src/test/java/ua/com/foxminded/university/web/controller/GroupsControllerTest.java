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
import ua.com.foxminded.university.exceptions.ObjectNotFoundById;
import ua.com.foxminded.university.web.exceptions.RestResponseEntityExceptionHandler;

@ExtendWith(MockitoExtension.class)
class GroupsControllerTest {

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

    @Spy
    private RestResponseEntityExceptionHandler controllerAdvice =
        new RestResponseEntityExceptionHandler();

    @InjectMocks
    private GroupsController groupController;

    private MockMvc mockMvc;

    private final static int GROUP_TEST_ID = 0;

    private static final long COURSE_TEST_ID = 0;

    private static final long TEACHER_TEST_ID = 0;

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
        verify(groupService).getAll();
    }

    @Test
    void shouldGetByIdWhenGetWithId() throws Exception {
        Group testGroup = new Group("test");
        Set<Student> students = new HashSet<>();
        when(groupService.getById(GROUP_TEST_ID)).thenReturn(testGroup);
        when(groupService.getStudents(testGroup)).thenReturn(students);
        mockMvc.perform(get("/groups/" + GROUP_TEST_ID + "/edit"))
            .andExpect(status().isOk());
        verify(groupService).getById(GROUP_TEST_ID);
    }

    @Test
    void shouldShowCreatingNew() throws Exception {
        mockMvc.perform(get("/groups/new"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldHandleEmptyDataAccesExceptionWhenGroupNotExist() throws Exception {
        ObjectNotFoundById ex = new ObjectNotFoundById(GROUP_TEST_ID, Group.class);
        when(groupService.getById(GROUP_TEST_ID))
            .thenThrow(ex);
        mockMvc.perform(get("/groups/" + GROUP_TEST_ID + "/edit"))
            .andExpect(status().isNotFound());
        verify(controllerAdvice).handleObjectNotFoundById(Mockito.eq(ex), Mockito.any());
    }

    @Test
    void shouldCreateNewGroup() throws Exception {
        Group testGroup = new Group("test");
        RequestBuilder request = post("/groups")
                .flashAttr("group", testGroup);
        mockMvc.perform(request)
            .andExpect(status().is3xxRedirection());
        verify(groupService).save(testGroup);
    }

    @Test
    void shouldUpdateGroup() throws Exception {
        Group testGroup = new Group("test");
        testGroup.setId(GROUP_TEST_ID);
        RequestBuilder request = patch("/groups/" + GROUP_TEST_ID)
                .flashAttr("group", testGroup);
        mockMvc.perform(request)
            .andExpect(status().is3xxRedirection());
        verify(groupService).save(testGroup);
    }

    @Test
    void shouldDeleteGroup() throws Exception {
        Group testGroup = new Group("test");
        testGroup.setId(GROUP_TEST_ID);
        RequestBuilder request = delete("/groups/" + GROUP_TEST_ID)
                .flashAttr("group", testGroup);
        mockMvc.perform(request)
            .andExpect(status().is3xxRedirection());
        verify(groupService).delete(GROUP_TEST_ID);
    }

    @Test
    void shouldShowAddingACourse() throws Exception {
        Group testGroup = new Group("test");
        when(groupService.getById(GROUP_TEST_ID)).thenReturn(testGroup);
        when(courseService.getAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/groups/" + GROUP_TEST_ID + "/add-course"))
            .andExpect(status().isOk());
        verify(groupService).getById(GROUP_TEST_ID);
        verify(courseService).getAll();
    }

    @Test
    void shouldShowDeletingACourse() throws Exception {
        Group testGroup = new Group("test");
        when(groupService.getById(GROUP_TEST_ID)).thenReturn(testGroup);
        when(groupService.getCourses(testGroup)).thenReturn(new HashSet<>());
        mockMvc.perform(get("/groups/" + GROUP_TEST_ID + "/delete-course"))
            .andExpect(status().isOk());
        verify(groupService).getById(GROUP_TEST_ID);
        verify(groupService).getCourses(testGroup);
    }

    @Test
    void shouldDeleteACourse() throws Exception {
        Group testGroup = new Group("test");
        testGroup.setId(GROUP_TEST_ID);

        Course testCourse = new Course();
        testCourse.setId(COURSE_TEST_ID);
        testCourse.setName("test");

        when(groupService.getById(testGroup.getId())).thenReturn(testGroup);
        when(courseService.getById(testCourse.getId())).thenReturn(testCourse);

        mockMvc.perform(delete("/groups/delete-course")
                .param("groupId", "" + testGroup.getId())
                .param("courseId", "" + testCourse.getId()))
            .andExpect(status().is3xxRedirection());
        verify(groupService).removeFromCourse(testGroup, testCourse);
    }

    @Test
    void shouldAddCourses() throws Exception {
        Group testGroup = new Group("test");
        testGroup.setId(GROUP_TEST_ID);

        Course testCourse = new Course();
        testCourse.setId(COURSE_TEST_ID);
        testCourse.setName("test");

        when(groupService.getById(testGroup.getId())).thenReturn(testGroup);
        when(courseService.getById(testCourse.getId())).thenReturn(testCourse);

        mockMvc.perform(post("/groups/add-course")
                .param("groupId", "" + testGroup.getId())
                .param("courseId", "" + testCourse.getId()))
            .andExpect(status().is3xxRedirection());
        verify(groupService).addToCourses(Mockito.any(), Mockito.any());
    }

    @Test
    void shouldShowTabletime() throws Exception {
        Group group = new Group();
        group.setName("test group");
        group.setId(GROUP_TEST_ID);

        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        TabletimeRow tabletimeRow = new TabletimeRow(end, new Course(), group, new Teacher());

        when(groupService.getById(group.getId())).thenReturn(group);
        when(tabletimeService.getTabletimeForGroup(group.getId(), begin, end))
            .thenReturn(Collections.singletonList(tabletimeRow));

        mockMvc.perform(get("/groups/" + group.getId() + "/tabletime")
                .param("begin", begin.toString())
                .param("end", end.toString()))
            .andExpect(status().isOk());
    }

    @Test
    void shouldAddingNewRecordToTabletime() throws Exception {
        Group group = new Group();
        group.setName("test group");
        group.setId(GROUP_TEST_ID);

        Course course = new Course();
        course.setName("test course");
        course.setId(COURSE_TEST_ID);

        when(groupService.getById(group.getId())).thenReturn(group);
        when(groupService.getCourses(group)).thenReturn(Collections.singleton(course));

        mockMvc.perform(get("/groups/" + group.getId() + "/tabletime/new"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreateNewRecordToTabletime() throws Exception {
        Group group = new Group();
        group.setName("test group");
        group.setId(GROUP_TEST_ID);

        Course course = new Course();
        course.setName("test course");
        course.setId(COURSE_TEST_ID);

        Teacher teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.setId(TEACHER_TEST_ID);

        when(groupService.getById(group.getId())).thenReturn(group);
        when(courseService.getById(course.getId())).thenReturn(course);
        when(teacherService.getById(teacher.getId())).thenReturn(teacher);

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
