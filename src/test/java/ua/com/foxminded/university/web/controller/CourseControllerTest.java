package ua.com.foxminded.university.web.controller;

import static org.mockito.Mockito.verify;
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

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.web.exceptions.RestResponseEntityExceptionHandler;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    private static final long COURSE_TEST_ID = 0;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @Spy
    private RestResponseEntityExceptionHandler controllerAdvice =
        new RestResponseEntityExceptionHandler();

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setControllerAdvice(controllerAdvice)
                .build();
    }

    @Test
    void shouldGetCoursesListWhenGetCourses() throws Exception {
        mockMvc.perform(get("/courses"))
            .andExpect(status().isOk());
        verify(courseService).getAll();
    }

    @Test
    void shouldGetByIdWhenGetWithId() throws Exception {
        int id = 1;
        mockMvc.perform(get("/courses/" + id + "/edit"))
            .andExpect(status().isOk());
        verify(courseService).getById(id);
    }

    @Test
    void shouldShowCreatingNew() throws Exception {
        mockMvc.perform(get("/courses/new"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreateNew() throws Exception {
        Course course = new Course();
        course.setName("test course");
        course.setId(COURSE_TEST_ID);

        mockMvc.perform(post("/courses")
                .flashAttr("course", course))
            .andExpect(status().is3xxRedirection());
        verify(courseService).save(course);
    }

    @Test
    void shouldUpdate() throws Exception {
        Course course = new Course();
        course.setName("test course");
        course.setId(COURSE_TEST_ID);

        mockMvc.perform(patch("/courses/" + COURSE_TEST_ID)
                .flashAttr("course", course))
            .andExpect(status().is3xxRedirection());
        verify(courseService).update(course);
    }

    @Test
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/courses/" + COURSE_TEST_ID))
            .andExpect(status().is3xxRedirection());
        verify(courseService).delete(COURSE_TEST_ID);
    }

}
