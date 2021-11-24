package ua.com.foxminded.university.web.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.TeacherService;

@ExtendWith(MockitoExtension.class)
class TeachersControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private TeachersController teachersController;

    private MockMvc mockMvc;
    private final static int TEACHER_TEST_ID = 1;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(teachersController).build();
    }

    @Test
    void shouldGetTeachersListWhenGetTeachers() throws Exception {
        mockMvc.perform(get("/teachers"))
            .andExpect(status().isOk());
        verify(teacherService).getAll();
    }

    @Test
    void shouldGetByIdWhenGetWithId() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        when(teacherService.getById(TEACHER_TEST_ID)).thenReturn(teacher);
        mockMvc.perform(get("/teachers/" + TEACHER_TEST_ID + "/edit"))
            .andExpect(status().isOk());
        verify(teacherService).getById(TEACHER_TEST_ID);
    }

}
