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
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.StudentService;
import ua.com.foxminded.university.web.dto.StudentDto;
import ua.com.foxminded.university.web.exceptions.RestResponseEntityExceptionHandler;
import ua.com.foxminded.university.web.mapper.StudentMapper;

@ExtendWith(MockitoExtension.class)
class StudentsRestControllerTest {

    private static final int OFFSET = 0;
    private static final int LIMIT = 100;
    private static final String GROUP_NAME = "test group";
    private static final long GROUP_ID = 1;
    private static final long STUDENT_ID = 1;
    private static final String STUDENT_LAST_NAME = "student";
    private static final String STUDENT_FIRST_NAME = "Test";

    @Mock
    private StudentService studentService;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private GroupService groupService;

    @Spy
    private RestResponseEntityExceptionHandler controllerAdvice = new RestResponseEntityExceptionHandler();

    @InjectMocks
    private StudentsRestController studentsRestController;

    private ObjectMapper mapper = new ObjectMapper();;
    private MockMvc mockMvc;

    @BeforeEach
    private void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentsRestController)
                .setControllerAdvice(controllerAdvice)
                .build();
    }

    @Test
    void shouldGetAllStudentWhenGetRequest() throws Exception {
        StudentDto studentDto = buildStudentDto();
        Student student = buildStudent();
        Page<Student> page = new PageImpl<>(Collections.singletonList(student));

        when(studentService.findAll(Mockito.any(PageRequest.class))).thenReturn(page);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        mockMvc.perform(get("/students-rest")
                .param("limit", "" + LIMIT)
                .param("offset", "" + OFFSET))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", Is.is((int) STUDENT_ID)))
            .andExpect(jsonPath("$[0].firstName", Is.is(STUDENT_FIRST_NAME)))
            .andExpect(jsonPath("$[0].lastName", Is.is(STUDENT_LAST_NAME)));
    }

    @Test
    void shouldGetStudentWhenGetStudentRequest() throws Exception {
        Student student = buildStudent();
        StudentDto studentDto = buildStudentDto();

        when(studentService.findById(STUDENT_ID)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        mockMvc.perform(get("/students-rest/" + STUDENT_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Is.is((int) STUDENT_ID)))
            .andExpect(jsonPath("$.firstName", Is.is(STUDENT_FIRST_NAME)))
            .andExpect(jsonPath("$.lastName", Is.is(STUDENT_LAST_NAME)));
    }

    @Test
    void shouldIsOkWhenCreateRequest() throws Exception {
        Group group = buildGroup();
        StudentDto studentDto = buildStudentDto();
        studentDto.setGroupId(GROUP_ID);

        when(groupService.findById(GROUP_ID)).thenReturn(group);

        String json = mapper.writeValueAsString(studentDto);
        mockMvc.perform(post("/students-rest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    void shouldIsOkWhenUpdateRequest() throws Exception {
        Group group = buildGroup();
        StudentDto studentDto = buildStudentDto();
        studentDto.setGroupId(GROUP_ID);

        when(groupService.findById(GROUP_ID)).thenReturn(group);

        String json = mapper.writeValueAsString(studentDto);
        mockMvc.perform(patch("/students-rest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    void shouldIsOkWhenDeleteRequest() throws Exception {
        mockMvc.perform(delete("/students-rest")
                .param("id", "" + STUDENT_ID))
            .andExpect(status().isOk());
    }

    private StudentDto buildStudentDto() {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(STUDENT_ID);
        studentDto.setFirstName(STUDENT_FIRST_NAME);
        studentDto.setLastName(STUDENT_LAST_NAME);
        return studentDto;
    }

    private Student buildStudent() {
        Student student = new Student();
        student.setId(STUDENT_ID);
        student.setFirstName(STUDENT_FIRST_NAME);
        student.setLastName(STUDENT_LAST_NAME);
        return student;
    }

    private Group buildGroup() {
        Group group = new Group();
        group.setId(GROUP_ID);
        group.setName(GROUP_NAME);
        return group;
    }

}
