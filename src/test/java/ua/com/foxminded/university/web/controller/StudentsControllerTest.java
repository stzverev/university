package ua.com.foxminded.university.web.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import ua.com.foxminded.university.web.dto.GroupDto;
import ua.com.foxminded.university.web.dto.StudentDto;
import ua.com.foxminded.university.web.exceptions.RestResponseEntityExceptionHandler;
import ua.com.foxminded.university.web.mapper.GroupMapper;
import ua.com.foxminded.university.web.mapper.StudentMapper;

@ExtendWith(MockitoExtension.class)
class StudentsControllerTest {

    private static final String STUDENT_LAST_NAME = "Student";
    private static final String STUDENT_FIRST_NAME = "Test";
    private static final String GROUP_NAME = "test";
    private static final long STUDENT_TEST_ID = 1L;
    private static final long GROUP_ID = 1L;

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

    private static final long STUDENT_ID = 1L;

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
        Student student = buildTestStudent();
        Group group = buildTestGroup();
        StudentDto studentDto = buildTestStudentDto();

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
        StudentDto studentDto = buildTestStudentDto();
        Group group = buildTestGroup();
        Student student = buildTestStudent();
        when(groupService.findById(GROUP_ID)).thenReturn(group);
        when(studentMapper.toEntity(studentDto, group)).thenReturn(student);

        mockMvc.perform(patch("/students/" + studentDto.getId())
                .flashAttr("student", studentDto)
                .param("groupId", "" + GROUP_ID))
            .andExpect(status().is3xxRedirection());
        verify(studentService).save(student);
    }

    private Group buildTestGroup() {
        Group group = new Group(GROUP_NAME);
        group.setId(GROUP_ID);
        return group;
    }

    private Student buildTestStudent() {
        Student student = new Student();
        student.setFirstName(STUDENT_FIRST_NAME);
        student.setLastName(STUDENT_LAST_NAME);
        student.setId(STUDENT_TEST_ID);
        return student;
    }

    private StudentDto buildTestStudentDto() {
        StudentDto student = new StudentDto();
        student.setFirstName(STUDENT_FIRST_NAME);
        student.setLastName(STUDENT_LAST_NAME);
        student.setId(STUDENT_TEST_ID);
        return student;
    }

    private GroupDto buildTestGroupDto() {
        GroupDto groupDto = new GroupDto();
        groupDto.setName(GROUP_NAME);
        groupDto.setId(GROUP_ID);
        return groupDto;
    }

    @Test
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/students/" + STUDENT_TEST_ID))
            .andExpect(status().is3xxRedirection());
        verify(studentService).deleteById(STUDENT_TEST_ID);
    }

}
