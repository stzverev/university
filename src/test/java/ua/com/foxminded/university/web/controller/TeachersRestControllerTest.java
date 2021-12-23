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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.web.dto.TeacherDto;
import ua.com.foxminded.university.web.exceptions.RestResponseEntityExceptionHandler;
import ua.com.foxminded.university.web.mapper.TeacherMapper;

@ExtendWith(MockitoExtension.class)
class TeachersRestControllerTest {

    private static final long TEACHER_ID = 1;
    private static final String TEACHER_LAST_NAME = "teacher";
    private static final String TEACHER_FIRST_NAME = "Test";

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @Mock
    private GroupService groupService;

    @Spy
    private RestResponseEntityExceptionHandler controllerAdvice = new RestResponseEntityExceptionHandler();

    @InjectMocks
    private TeachersRestController teachersRestController;

    private ObjectMapper mapper = new ObjectMapper();;
    private MockMvc mockMvc;

    @BeforeEach
    private void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(teachersRestController)
                .setControllerAdvice(controllerAdvice)
                .build();
    }

    @Test
    void shouldGetAllTeacherWhenGetRequest() throws Exception {
        TeacherDto teacherDto = buildTeacherDto();
        Teacher teacher = buildTeacher();

        when(teacherService.findAll()).thenReturn(Collections.singletonList(teacher));
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        mockMvc.perform(get("/teachers-rest"))
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

        mockMvc.perform(get("/teachers-rest/" + TEACHER_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Is.is((int) TEACHER_ID)))
            .andExpect(jsonPath("$.firstName", Is.is(TEACHER_FIRST_NAME)))
            .andExpect(jsonPath("$.lastName", Is.is(TEACHER_LAST_NAME)));
    }

    @Test
    void shouldIsOkWhenCreateRequest() throws Exception {
        TeacherDto teacherDto = buildTeacherDto();

        String json = mapper.writeValueAsString(teacherDto);
        mockMvc.perform(post("/teachers-rest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    void shouldIsOkWhenUpdateRequest() throws Exception {
        TeacherDto teacherDto = buildTeacherDto();

        String json = mapper.writeValueAsString(teacherDto);
        mockMvc.perform(patch("/teachers-rest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    void shouldIsOkWhenDeleteRequest() throws Exception {
        mockMvc.perform(delete("/teachers-rest")
                .param("id", "" + TEACHER_ID))
            .andExpect(status().isOk());
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
