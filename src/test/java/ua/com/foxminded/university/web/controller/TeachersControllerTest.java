package ua.com.foxminded.university.web.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.university.data.Config;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.web.config.WebConfig;

@ContextConfiguration(classes = {WebConfig.class, Config.class})
@WebMvcTest(controllers = {TeachersController.class})
class TeachersControllerTest {

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private CourseService courseService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetTeachersListWhenGetTeachers() throws Exception {
        mockMvc.perform(get("/teachers"))
            .andExpect(status().isOk());
        verify(teacherService).getAll();
    }

}
