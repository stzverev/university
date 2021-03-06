package ua.com.foxminded.university.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.web.dto.TeacherDto;
import ua.com.foxminded.university.web.mapper.TeacherMapper;

@WebMvcTest(TeachersRestController.class)
class TeachersRestControllerTest {

    private static final int OFFSET = 0;
    private static final int LIMIT = 100;
    private static final String COURSE_NAME = "test course";
    private static final long COURSE_ID = 1L;
    private static final String REQUEST_MAIN = "/teachers-rest";
    private static final long TEACHER_ID = 1;
    private static final String REQUEST_COURSES = REQUEST_MAIN + "/" + TEACHER_ID + "/courses";
    private static final String TEACHER_LAST_NAME = "teacher";
    private static final String TEACHER_FIRST_NAME = "Test";

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private TeacherMapper teacherMapper;

    @MockBean
    private GroupService groupService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();;

    @Test
    void shouldGetAllTeacherWhenGetRequest() throws Exception {
        TeacherDto teacherDto = buildTeacherDto();
        Teacher teacher = buildTeacher();
        Page<Teacher> page = new PageImpl<>(Collections.singletonList(teacher));

        when(teacherService.findAll(Mockito.any(PageRequest.class))).thenReturn(page);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        mockMvc.perform(get(REQUEST_MAIN)
                .param("limit", "" + LIMIT)
                .param("offset", "" + OFFSET))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", Is.is((int) TEACHER_ID)))
            .andExpect(jsonPath("$[0].firstName", Is.is(TEACHER_FIRST_NAME)))
            .andExpect(jsonPath("$[0].lastName", Is.is(TEACHER_LAST_NAME)));
    }

    @Test
    void shouldGetTeacherWhenGetTeacherRequest() throws Exception {
        Teacher teacher = buildTeacher();
        TeacherDto teacherDto = buildTeacherDto();

        when(teacherService.findById(TEACHER_ID)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        mockMvc.perform(get(REQUEST_MAIN + "/" + TEACHER_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Is.is((int) TEACHER_ID)))
            .andExpect(jsonPath("$.firstName", Is.is(TEACHER_FIRST_NAME)))
            .andExpect(jsonPath("$.lastName", Is.is(TEACHER_LAST_NAME)));
    }

    @Test
    void shouldIsOkWhenCreateRequest() throws Exception {
        TeacherDto teacherDto = buildTeacherDto();

        String json = mapper.writeValueAsString(teacherDto);
        mockMvc.perform(post(REQUEST_MAIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    void shouldIsOkWhenUpdateRequest() throws Exception {
        TeacherDto teacherDto = buildTeacherDto();

        String json = mapper.writeValueAsString(teacherDto);
        mockMvc.perform(patch(REQUEST_MAIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    void shouldIsOkWhenDeleteRequest() throws Exception {
        mockMvc.perform(delete(REQUEST_MAIN)
                .param("id", "" + TEACHER_ID))
            .andExpect(status().isOk());
    }

    @Test
    void shouldIsOkWhenAddCourse() throws Exception {
        Teacher teacher = buildTeacher();
        Course course = buildCourse();

        when(teacherService.findById(TEACHER_ID)).thenReturn(teacher);
        when(courseService.findById(COURSE_ID)).thenReturn(course);

        mockMvc.perform(post(REQUEST_COURSES)
                .param("courseId", "" + COURSE_ID))
            .andExpect(status().isOk());
    }

    @Test
    void shouldIsOkWhenDeleteCourse() throws Exception {
        Teacher teacher = buildTeacher();
        Course course = buildCourse();

        when(teacherService.findById(TEACHER_ID)).thenReturn(teacher);
        when(courseService.findById(COURSE_ID)).thenReturn(course);

        mockMvc.perform(delete(REQUEST_COURSES)
                .param("courseId", "" + COURSE_ID))
            .andExpect(status().isOk());
    }

    private Course buildCourse() {
        Course course = new Course();
        course.setId(COURSE_ID);
        course.setName(COURSE_NAME);
        return course;
    }

    private TeacherDto buildTeacherDto() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(TEACHER_ID);
        teacherDto.setFirstName(TEACHER_FIRST_NAME);
        teacherDto.setLastName(TEACHER_LAST_NAME);
        return teacherDto;
    }

    private Teacher buildTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(TEACHER_ID);
        teacher.setFirstName(TEACHER_FIRST_NAME);
        teacher.setLastName(TEACHER_LAST_NAME);
        return teacher;
    }

}
