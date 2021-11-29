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
import ua.com.foxminded.university.web.exceptions.RestResponseEntityExceptionHandler;

@ExtendWith(MockitoExtension.class)
class StudentsControllerTest {

    private static final long STUDENT_TEST_ID = 0;

    private static final long GROUP_TEST_ID = 0;

    @Mock
    private GroupService groupService;

    @Mock
    private StudentService studentService;

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
        verify(studentService).getAll();
    }

    @Test
    void shouldGetByIdWhenGetWithId() throws Exception {
        int id = 1;
        mockMvc.perform(get("/students/" + id + "/edit"))
            .andExpect(status().isOk());
        verify(studentService).getById(id);
    }

    @Test
    void shouldShowCreatingNew() throws Exception {
        mockMvc.perform(get("/students/new"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreateNew() throws Exception {
        Student student = new Student();
        student.setFirstName("Test");
        student.setLastName("Student");
        student.setId(STUDENT_TEST_ID);

        Group group = new Group("test");
        group.setId(GROUP_TEST_ID);

        when(groupService.getById(GROUP_TEST_ID)).thenReturn(group);

        mockMvc.perform(post("/students")
                .flashAttr("student", student)
                .param("groupId", "" + group.getId()))
            .andExpect(status().is3xxRedirection());
        verify(studentService).save(student);
    }

    @Test
    void shouldUpdate() throws Exception {
        Student student = new Student();
        student.setFirstName("Test");
        student.setLastName("Student");
        student.setId(STUDENT_TEST_ID);

        Group group = new Group("test");
        group.setId(GROUP_TEST_ID);

        mockMvc.perform(patch("/students/" + student.getId())
                .flashAttr("student", student)
                .param("groupId", "" + group.getId()))
            .andExpect(status().is3xxRedirection());
        verify(studentService).update(student);
    }

    @Test
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/students/" + STUDENT_TEST_ID))
            .andExpect(status().is3xxRedirection());
        verify(studentService).delete(STUDENT_TEST_ID);
    }

}
