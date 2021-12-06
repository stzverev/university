package ua.com.foxminded.university.web.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.web.exceptions.RestResponseEntityExceptionHandler;

@ExtendWith(MockitoExtension.class)
class TeachersControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private TeachersController teachersController;

    @Spy
    private RestResponseEntityExceptionHandler controllerAdvice =
        new RestResponseEntityExceptionHandler();

    private MockMvc mockMvc;
    private final static int TEACHER_TEST_ID = 1;

    private static final long COURSE_TEST_ID = 0;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(teachersController)
                .setControllerAdvice(controllerAdvice)
                .build();
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

    @Test
    void shouldShowCreatingNew() throws Exception {
        mockMvc.perform(get("/teachers/new"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreateNew() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.setId(TEACHER_TEST_ID);

        mockMvc.perform(post("/teachers")
                .flashAttr("teacher", teacher))
            .andExpect(status().is3xxRedirection());
        verify(teacherService).save(teacher);
    }

    @Test
    void shouldUpdate() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.setId(TEACHER_TEST_ID);

        mockMvc.perform(patch("/teachers/" + teacher.getId())
                .flashAttr("teacher", teacher))
            .andExpect(status().is3xxRedirection());
        verify(teacherService).update(teacher);
    }

    @Test
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/teachers/" + TEACHER_TEST_ID))
            .andExpect(status().is3xxRedirection());
        verify(teacherService).delete(TEACHER_TEST_ID);
    }

    @Test
    void shouldShowAddingACourse() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.setId(TEACHER_TEST_ID);

        when(teacherService.getById(teacher.getId())).thenReturn(teacher);
        when(courseService.getAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/teachers/" + teacher.getId() + "/add-course"))
            .andExpect(status().isOk());
        verify(teacherService).getById(teacher.getId());
        verify(courseService).getAll();
    }

    @Test
    void shouldShowDeletingACourse() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.setId(TEACHER_TEST_ID);

        when(teacherService.getById(teacher.getId())).thenReturn(teacher);
        when(teacherService.getCourses(teacher)).thenReturn(new HashSet<>());
        mockMvc.perform(get("/teachers/" + teacher.getId() + "/delete-course"))
            .andExpect(status().isOk());
        verify(teacherService).getById(teacher.getId());
        verify(teacherService).getCourses(teacher);
    }

    @Test
    void shouldDeleteACourse() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.setId(TEACHER_TEST_ID);

        Course testCourse = new Course();
        testCourse.setId(COURSE_TEST_ID);
        testCourse.setName("test");

        when(teacherService.getById(teacher.getId())).thenReturn(teacher);
        when(courseService.getById(testCourse.getId())).thenReturn(testCourse);

        mockMvc.perform(delete("/teachers/delete-course")
                .param("teacherId", "" + teacher.getId())
                .param("courseId", "" + testCourse.getId()))
            .andExpect(status().is3xxRedirection());
        verify(teacherService).removeFromCourse(teacher, testCourse);
    }

    @Test
    void shouldAddCourses() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Test");
        teacher.setLastName("Teacher");
        teacher.setId(TEACHER_TEST_ID);

        Course testCourse = new Course();
        testCourse.setId(COURSE_TEST_ID);
        testCourse.setName("test");

        when(teacherService.getById(teacher.getId())).thenReturn(teacher);
        when(courseService.getById(testCourse.getId())).thenReturn(testCourse);

        mockMvc.perform(post("/teachers/add-course")
                .param("teacherId", "" + teacher.getId())
                .param("courseId", "" + testCourse.getId()))
            .andExpect(status().is3xxRedirection());
        verify(teacherService).addToCourse(Mockito.any(), Mockito.any());
    }

}
