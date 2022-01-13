package ua.com.foxminded.university.web.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

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
import ua.com.foxminded.university.web.dto.TabletimeDto;
import ua.com.foxminded.university.web.mapper.CourseMapper;
import ua.com.foxminded.university.web.mapper.GroupMapper;
import ua.com.foxminded.university.web.mapper.StudentMapper;
import ua.com.foxminded.university.web.mapper.TabletimeMapper;
import ua.com.foxminded.university.web.mapper.TeacherMapper;

@WebMvcTest(GroupsController.class)
class GroupsControllerTest {

    private static final int GROUP_NAME_MAX_LENGTH = 150;
    private static final String VALID_ERROR_GROUP_NAME_BLANK = "must not be blank";
    private static final String VALID_ERROR_GROUP_NAME_SIZE = ""
            + "size must be between 0 and " + GROUP_NAME_MAX_LENGTH;
    private static final String TEACHER_FIRST_NAME = "Test";
    private static final String TEACHER_LAST_NAME = "Teacher";
    private static final String COURSE_NAME = "test course";
    private static final String GROUP_NAME = "test";
    private final static long GROUP_ID = 1L;
    private static final long COURSE_ID = 1L;
    private static final long TEACHER_ID = 1L;

    @MockBean
    private GroupService groupService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private TabletimeService tabletimeService;

    @MockBean
    private StudentService studentService;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private GroupMapper groupMapper;

    @MockBean
    private StudentMapper studentMapper;

    @MockBean
    private CourseMapper courseMapper;

    @MockBean
    private TeacherMapper teacherMapper;

    @MockBean
    private TabletimeMapper tabletimeMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetGroupsListWhenGetGroups() throws Exception {
        mockMvc.perform(get("/groups"))
            .andExpect(status().isOk());
        verify(groupService).findAll();
    }

    @Test
    void shouldGetByIdWhenGetWithId() throws Exception {
        Group group = buildGroup();
        Set<Student> students = new HashSet<>();
        when(groupService.findById(GROUP_ID)).thenReturn(group);
        when(groupService.findStudents(group)).thenReturn(students);
        when(groupMapper.toDto(group)).thenReturn(buildGroupDto());
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
    }

    @Test
    void shouldCreateNewGroup() throws Exception {
        Group group = buildGroup();
        GroupDto groupDto = buildGroupDto();
        when(groupMapper.toEntity(groupDto)).thenReturn(group);

        RequestBuilder request = post("/groups")
                .flashAttr("group", groupDto);
        mockMvc.perform(request)
            .andExpect(status().is3xxRedirection());
        verify(groupService).save(group);
    }

    @Test
    void shouldUpdateGroup() throws Exception {
        Group group = buildGroup();
        GroupDto groupDto = buildGroupDto();
        when(groupMapper.toEntity(groupDto)).thenReturn(group);

        RequestBuilder request = patch("/groups/" + GROUP_ID)
                .flashAttr("group", groupDto);
        mockMvc.perform(request)
            .andExpect(status().is3xxRedirection());
        verify(groupService).save(group);
    }

    @Test
    void shouldDeleteGroup() throws Exception {
        Group group = buildGroup();
        RequestBuilder request = delete("/groups/" + GROUP_ID)
                .flashAttr("group", group);
        mockMvc.perform(request)
            .andExpect(status().is3xxRedirection());
        verify(groupService).deleteById(GROUP_ID);
    }

    @Test
    void shouldShowAddingACourse() throws Exception {
        Group group = buildGroup();
        when(groupService.findById(GROUP_ID)).thenReturn(group);
        when(courseService.findAll()).thenReturn(new ArrayList<>());
        when(groupMapper.toDto(group)).thenReturn(buildGroupDto());
        mockMvc.perform(get("/groups/" + GROUP_ID + "/add-course"))
            .andExpect(status().isOk());
        verify(groupService).findById(GROUP_ID);
        verify(courseService).findAll();
    }

    @Test
    void shouldShowDeletingACourse() throws Exception {
        Group group = buildGroup();
        when(groupService.findWithCoursesById(GROUP_ID)).thenReturn(group);
        when(groupService.findCourses(group)).thenReturn(new HashSet<>());
        when(groupMapper.toDto(group)).thenReturn(buildGroupDto());

        mockMvc.perform(get("/groups/" + GROUP_ID + "/delete-course"))
            .andExpect(status().isOk());
        verify(groupService).findWithCoursesById(GROUP_ID);
        verify(groupService).findCourses(group);
    }

    @Test
    void shouldDeleteACourse() throws Exception {
        Group group = buildGroup();
        Course course = buildCourse();

        when(groupService.findById(group.getId())).thenReturn(group);
        when(courseService.findById(course.getId())).thenReturn(course);

        mockMvc.perform(delete("/groups/delete-course")
                .param("groupId", "" + group.getId())
                .param("courseId", "" + course.getId()))
            .andExpect(status().is3xxRedirection());
        verify(groupService).removeFromCourse(group, course);
    }

    @Test
    void shouldAddCourses() throws Exception {
        Group group = buildGroup();
        Course course = buildCourse();

        when(groupService.findById(group.getId())).thenReturn(group);
        when(courseService.findById(course.getId())).thenReturn(course);

        mockMvc.perform(post("/groups/add-course")
                .param("groupId", "" + group.getId())
                .param("courseId", "" + course.getId()))
            .andExpect(status().is3xxRedirection());
        verify(groupService).addToCourse(Mockito.any(), Mockito.any());
    }

    @Test
    void shouldShowTabletime() throws Exception {
        Group group = buildGroup();
        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        TabletimeRow tabletimeRow = buildTabletimeRow(group, end);

        when(groupService.findById(group.getId())).thenReturn(group);
        when(tabletimeService.getTabletimeForGroup(group.getId(), begin, end))
            .thenReturn(Collections.singletonList(tabletimeRow));
        when(groupMapper.toDto(group)).thenReturn(buildGroupDto());
        when(tabletimeMapper.toDto(tabletimeRow)).thenReturn(buildTabletimeDto(end));

        mockMvc.perform(get("/groups/" + group.getId() + "/tabletime")
                .param("begin", begin.toString())
                .param("end", end.toString()))
            .andExpect(status().isOk());
    }

    private TabletimeRow buildTabletimeRow(Group group, LocalDateTime dateTime) {
        return new TabletimeRow(dateTime, new Course(), group, new Teacher());
    }

    private TabletimeDto buildTabletimeDto(LocalDateTime dateTime) {
        return new TabletimeDto(dateTime, GROUP_NAME, COURSE_NAME, TEACHER_FIRST_NAME, TEACHER_LAST_NAME);
    }

    @Test
    void shouldAddingNewRecordToTabletime() throws Exception {
        Course course = buildCourse();
        Group group = buildGroup();
        group.setCourses(Collections.singleton(course));
        GroupDto groupDto = buildGroupDto();
        CourseDto courseDto = buildCourseDto();

        when(groupMapper.toDto(group)).thenReturn(groupDto);
        when(courseMapper.toDto(course)).thenReturn(courseDto);
        when(groupService.findWithCoursesById(GROUP_ID)).thenReturn(group);

        mockMvc.perform(get("/groups/" + GROUP_ID + "/tabletime/new"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreateNewRecordToTabletime() throws Exception {
        Group group = buildGroup();
        Course course = buildCourse();
        Teacher teacher = buldTeacher();

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
        verify(tabletimeService).save(tabletimeRow);
    }

    @Test
    void shouldBadRequestWhenSaveGroupWithNameLengthMoreMaxLength() throws Exception {
        GroupDto groupDto = buildGroupDto();
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, GROUP_NAME_MAX_LENGTH + 1).forEach(i -> builder.append('a'));
        groupDto.setName(builder.toString());
        assertThat(groupDto.getName().length(), greaterThan(GROUP_NAME_MAX_LENGTH));

        mockMvc.perform(post("/groups")
                .flashAttr("group", groupDto))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name", Is.is(VALID_ERROR_GROUP_NAME_SIZE)));
    }

    @Test
    void shouldBadRequestWhenSaveGroupWithBlankName() throws Exception {
        GroupDto groupDto = buildGroupDto();
        groupDto.setName("");

        mockMvc.perform(post("/groups")
                .flashAttr("group", groupDto))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name", Is.is(VALID_ERROR_GROUP_NAME_BLANK)));
    }

    private Teacher buldTeacher() {
        Teacher teacher = new Teacher();
        teacher.setFirstName(TEACHER_FIRST_NAME);
        teacher.setLastName(TEACHER_LAST_NAME);
        teacher.setId(TEACHER_ID);
        return teacher;
    }

    private CourseDto buildCourseDto() {
        CourseDto courseDto = new CourseDto();
        courseDto.setName(COURSE_NAME);
        courseDto.setId(COURSE_ID);
        return courseDto;
    }

    private Course buildCourse() {
        Course course = new Course();
        course.setName(COURSE_NAME);
        course.setId(COURSE_ID);
        return course;
    }

    private GroupDto buildGroupDto() {
        GroupDto groupDto = new GroupDto();
        groupDto.setId(GROUP_ID);
        groupDto.setName(GROUP_NAME);
        return groupDto;
    }

    private Group buildGroup() {
        Group group = new Group(GROUP_NAME);
        group.setId(GROUP_ID);
        return group;
    }

}
