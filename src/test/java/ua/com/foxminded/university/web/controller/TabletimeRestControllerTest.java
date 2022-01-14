package ua.com.foxminded.university.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.TabletimeService;
import ua.com.foxminded.university.web.dto.TabletimeDto;
import ua.com.foxminded.university.web.mapper.TabletimeMapper;

@WebMvcTest(TabletimeRestController.class)
class TabletimeRestControllerTest {

    private static final long COURSE_ID = 1L;
    private static final String COURSE_NAME = "test course";
    private static final long GROUP_ID = 1L;
    private static final String GROUP_NAME = "test group";
    private static final long TEACHER_ID = 1L;
    private static final String TEACHER_LAST_NAME = "Teacher";
    private static final String TEACHER_FIRST_NAME = "Test";
    private static final long TABLETIME_ID = 1L;
    private static final LocalDateTime BEGIN = LocalDateTime.of(2021, 12, 1, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2021, 12, 31, 23, 59);
    private static final LocalDateTime TABLETIME_DATETIME = LocalDateTime.of(2021, 12, 01, 0, 0);
    private static final String REQUEST_MAIN = "/tabletime-rest";
    private static final String REQUEST_FOR_TEACHERS = REQUEST_MAIN + "/teachers";
    private static final String REQUEST_FOR_COURSE = REQUEST_MAIN + "/courses";
    private static final String REQUEST_FOR_GROUP = REQUEST_MAIN + "/groups";

    @MockBean
    private TabletimeService tabletimeService;

    @MockBean
    private TabletimeMapper tabletimeMapper;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @BeforeEach
    private void init() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldIsOkWhenAddTabletime() throws Exception {
        TabletimeDto tabletimeDto = buildTabletimeDto();

        String json = mapper.writeValueAsString(tabletimeDto);

        mockMvc.perform(post(REQUEST_MAIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    void shouldIsOkWhenDeleteTabletime() throws Exception {
        mockMvc.perform(delete(REQUEST_MAIN)
                .param("id", "" + TABLETIME_ID))
            .andExpect(status().isOk());
    }

    @Test
    void shouldGetTabletimeForTeacher() throws Exception {
        TabletimeRow tabletimeRow = buildTabletimeRow();
        TabletimeDto tabletimeDto = buildTabletimeDto();

        when(tabletimeService.getTabletimeForTeacher(TEACHER_ID, BEGIN, END))
            .thenReturn(Collections.singletonList(tabletimeRow));
        when(tabletimeMapper.toDto(tabletimeRow))
            .thenReturn(tabletimeDto);

        mockMvc.perform(get(REQUEST_FOR_TEACHERS)
                .param("teacherId", "" + TEACHER_ID)
                .param("begin", BEGIN.toString())
                .param("end", END.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", Is.is((int) TABLETIME_ID)))
            .andExpect(jsonPath("$[0].teacherFirstName", Is.is(TEACHER_FIRST_NAME)))
            .andExpect(jsonPath("$[0].teacherLastName", Is.is(TEACHER_LAST_NAME)))
            .andExpect(jsonPath("$[0].courseName", Is.is(COURSE_NAME)))
            .andExpect(jsonPath("$[0].groupName", Is.is(GROUP_NAME)));
    }

    @Test
    void shouldGetTabletimeForCourse() throws Exception {
        TabletimeRow tabletimeRow = buildTabletimeRow();
        TabletimeDto tabletimeDto = buildTabletimeDto();

        when(tabletimeService.getTabletimeForCourse(COURSE_ID, BEGIN, END))
            .thenReturn(Collections.singletonList(tabletimeRow));
        when(tabletimeMapper.toDto(tabletimeRow))
            .thenReturn(tabletimeDto);

        mockMvc.perform(get(REQUEST_FOR_COURSE)
                .param("courseId", "" + COURSE_ID)
                .param("begin", BEGIN.toString())
                .param("end", END.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", Is.is((int) TABLETIME_ID)))
            .andExpect(jsonPath("$[0].teacherFirstName", Is.is(TEACHER_FIRST_NAME)))
            .andExpect(jsonPath("$[0].teacherLastName", Is.is(TEACHER_LAST_NAME)))
            .andExpect(jsonPath("$[0].courseName", Is.is(COURSE_NAME)))
            .andExpect(jsonPath("$[0].groupName", Is.is(GROUP_NAME)));
    }

    @Test
    void shouldGetTabletimeForGroup() throws Exception {
        TabletimeRow tabletimeRow = buildTabletimeRow();
        TabletimeDto tabletimeDto = buildTabletimeDto();

        when(tabletimeService.getTabletimeForGroup(GROUP_ID, BEGIN, END))
            .thenReturn(Collections.singletonList(tabletimeRow));
        when(tabletimeMapper.toDto(tabletimeRow))
            .thenReturn(tabletimeDto);

        mockMvc.perform(get(REQUEST_FOR_GROUP)
                .param("groupId", "" + GROUP_ID)
                .param("begin", BEGIN.toString())
                .param("end", END.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", Is.is((int) TABLETIME_ID)))
            .andExpect(jsonPath("$[0].teacherFirstName", Is.is(TEACHER_FIRST_NAME)))
            .andExpect(jsonPath("$[0].teacherLastName", Is.is(TEACHER_LAST_NAME)))
            .andExpect(jsonPath("$[0].courseName", Is.is(COURSE_NAME)))
            .andExpect(jsonPath("$[0].groupName", Is.is(GROUP_NAME)));
    }

    private TabletimeDto buildTabletimeDto() {
        TabletimeDto tabletimeDto = new TabletimeDto(TABLETIME_DATETIME,
                GROUP_NAME, COURSE_NAME, TEACHER_FIRST_NAME, TEACHER_LAST_NAME);
        tabletimeDto.setId(TABLETIME_ID);
        return tabletimeDto;
    }

    private TabletimeRow buildTabletimeRow() {
        Teacher teacher = buildTeacher();
        Group group = buildGroup();
        Course course = buildCourse();

        TabletimeRow tabletimeRow = new TabletimeRow();
        tabletimeRow.setId(TABLETIME_ID);
        tabletimeRow.setDateTime(TABLETIME_DATETIME);
        tabletimeRow.setTeacher(teacher);
        tabletimeRow.setGroup(group);
        tabletimeRow.setCourse(course);
        return tabletimeRow;
    }

    private Course buildCourse() {
        Course course = new Course();
        course.setId(COURSE_ID);
        course.setName(COURSE_NAME);
        return course;
    }

    private Group buildGroup() {
        Group group = new Group();
        group.setId(GROUP_ID);
        group.setName(GROUP_NAME);
        return group;
    }

    private Teacher buildTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(TEACHER_ID);
        teacher.setFirstName(TEACHER_FIRST_NAME);
        teacher.setLastName(TEACHER_LAST_NAME);
        return teacher;
    }

}
