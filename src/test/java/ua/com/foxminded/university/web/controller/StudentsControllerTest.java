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

import java.util.stream.IntStream;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.StudentService;
import ua.com.foxminded.university.web.dto.StudentDto;
import ua.com.foxminded.university.web.exceptions.RestResponseEntityExceptionHandler;
import ua.com.foxminded.university.web.mapper.GroupMapper;
import ua.com.foxminded.university.web.mapper.StudentMapper;

@ExtendWith(MockitoExtension.class)
class StudentsControllerTest {

    private static final int STUDENT_FIRST_NAME_MAX_LENGTH = 100;
    private static final String VALID_ERROR_STUDENT_FIRST_NAME_BLANK = "must not be blank";
    private static final String VALID_ERROR_STUDENT_FIRST_NAME_SIZE =
            "size must be between 0 and " + STUDENT_FIRST_NAME_MAX_LENGTH;
    private static final String STUDENT_LAST_NAME = "Student";
    private static final String STUDENT_FIRST_NAME = "Test";
    private static final String GROUP_NAME = "test";
    private static final long STUDENT_TEST_ID = 1L;
    private static final long GROUP_ID = 1L;
    private static final long STUDENT_ID = 1L;

    @Mock
    private GroupService groupService;

    @Mock
    private StudentService studentService;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    private StudentsController studentsController;

    @Spy
    private RestResponseEntityExceptionHandler controllerAdvice =
        new RestResponseEntityExceptionHandler();

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentsController)
                .setControllerAdvice(controllerAdvice)
                .build();
    }

    @Test
    void shouldGetStudentsListWhenGetStudents() throws Exception {
        mockMvc.perform(get("/students"))
            .andExpect(status().isOk());
        verify(studentService).findAll();
    }

    @Test
    void shouldGetByIdWhenGetWithId() throws Exception {

        mockMvc.perform(get("/students/" + STUDENT_ID + "/edit"))
            .andExpect(status().isOk());
        verify(studentService).findById(STUDENT_ID);
    }

    @Test
    void shouldShowCreatingNew() throws Exception {
        mockMvc.perform(get("/students/new"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreateNew() throws Exception {
        Student student = buildStudent();
        Group group = buildGroup();
        StudentDto studentDto = buildStudentDto();

        when(groupService.findById(GROUP_ID)).thenReturn(group);
        when(studentMapper.toEntity(studentDto, group)).thenReturn(student);

        mockMvc.perform(post("/students")
                .flashAttr("student", studentDto)
                .param("groupId", "" + GROUP_ID))
            .andExpect(status().is3xxRedirection());
        verify(studentService).save(student);
    }

    @Test
    void shouldUpdate() throws Exception {
        StudentDto studentDto = buildStudentDto();
        Group group = buildGroup();
        Student student = buildStudent();
        when(groupService.findById(GROUP_ID)).thenReturn(group);
        when(studentMapper.toEntity(studentDto, group)).thenReturn(student);

        mockMvc.perform(patch("/students/" + studentDto.getId())
                .flashAttr("student", studentDto)
                .param("groupId", "" + GROUP_ID))
            .andExpect(status().is3xxRedirection());
        verify(studentService).save(student);
    }

    @Test
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/students/" + STUDENT_TEST_ID))
            .andExpect(status().is3xxRedirection());
        verify(studentService).deleteById(STUDENT_TEST_ID);
    }

    @Test
    void shouldBadRequestWhenSaveStudentWithNameLengthMoreMaxLength() throws Exception {
        StudentDto studentDto = buildStudentDto();
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, STUDENT_FIRST_NAME_MAX_LENGTH + 1).forEach(i -> builder.append('a'));
        studentDto.setFirstName(builder.toString());
        assertThat(studentDto.getFirstName().length(), greaterThan(STUDENT_FIRST_NAME_MAX_LENGTH));

        mockMvc.perform(post("/students")
                .flashAttr("student", studentDto))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.firstName", Is.is(VALID_ERROR_STUDENT_FIRST_NAME_SIZE)));
    }

    @Test
    void shouldBadRequestWhenSaveStudentWithBlankName() throws Exception {
        StudentDto studentDto = buildStudentDto();
        studentDto.setFirstName("");

        mockMvc.perform(post("/students")
                .flashAttr("student", studentDto))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.firstName", Is.is(VALID_ERROR_STUDENT_FIRST_NAME_BLANK)));
    }

    private Group buildGroup() {
        Group group = new Group(GROUP_NAME);
        group.setId(GROUP_ID);
        return group;
    }

    private Student buildStudent() {
        Student student = new Student();
        student.setFirstName(STUDENT_FIRST_NAME);
        student.setLastName(STUDENT_LAST_NAME);
        student.setId(STUDENT_TEST_ID);
        return student;
    }

    private StudentDto buildStudentDto() {
        StudentDto student = new StudentDto();
        student.setFirstName(STUDENT_FIRST_NAME);
        student.setLastName(STUDENT_LAST_NAME);
        student.setId(STUDENT_TEST_ID);
        return student;
    }

}
