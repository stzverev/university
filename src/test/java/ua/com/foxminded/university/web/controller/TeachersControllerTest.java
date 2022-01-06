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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.IntStream;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.web.dto.TeacherDto;
import ua.com.foxminded.university.web.exceptions.RestResponseEntityExceptionHandler;
import ua.com.foxminded.university.web.mapper.TeacherMapper;

@ExtendWith(MockitoExtension.class)
class TeachersControllerTest {

    private static final String COURSE_NAME = "test";
    private static final String TEACHER_LAST_NAME = "Teacher";
    private static final String TEACHER_FIRST_NAME = "Test";
    private final static long TEACHER_ID = 1L;
    private static final long COURSE_ID = 1L;
    private static final int TEACHER_FIRST_NAME_MAX_LENGTH = 100;
    private static final String VALID_ERROR_TEACHER_FIRST_NAME_SIZE = ""
            + "size must be between 0 and " + TEACHER_FIRST_NAME_MAX_LENGTH;
    private static final String VALID_ERROR_TEACHER_FIRST_NAME_BLANK = "must not be blank";

    @Mock
    private TeacherService teacherService;

    @Mock
    private CourseService courseService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeachersController teachersController;

    private RestResponseEntityExceptionHandler controllerAdvice =
        new RestResponseEntityExceptionHandler();

    private MockMvc mockMvc;

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
        verify(teacherService).findAll();
    }

    @Test
    void shouldGetByIdWhenGetWithId() throws Exception {
        Teacher teacher = buildTeacher();
        when(teacherService.findById(TEACHER_ID)).thenReturn(teacher);
        mockMvc.perform(get("/teachers/" + TEACHER_ID + "/edit"))
            .andExpect(status().isOk());
        verify(teacherService).findById(TEACHER_ID);
    }

    @Test
    void shouldShowCreatingNew() throws Exception {
        mockMvc.perform(get("/teachers/new"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreateNew() throws Exception {
        Teacher teacher = buildTeacher();
        TeacherDto teacherDto = buildTeacherDto();
        when(teacherMapper.toEntity(teacherDto)).thenReturn(teacher);

        mockMvc.perform(post("/teachers")
                .flashAttr("teacher", teacherDto))
            .andExpect(status().is3xxRedirection());
        verify(teacherService).save(teacher);
    }

    @Test
    void shouldUpdate() throws Exception {
        Teacher teacher = buildTeacher();
        TeacherDto teacherDto = buildTeacherDto();
        when(teacherMapper.toEntity(teacherDto)).thenReturn(teacher);

        mockMvc.perform(patch("/teachers/" + TEACHER_ID)
                .flashAttr("teacher", teacherDto))
            .andExpect(status().is3xxRedirection());
        verify(teacherService).save(teacher);
    }

    @Test
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/teachers/" + TEACHER_ID))
            .andExpect(status().is3xxRedirection());
        verify(teacherService).deleteById(TEACHER_ID);
    }

    @Test
    void shouldShowAddingACourse() throws Exception {
        Teacher teacher = buildTeacher();

        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(courseService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/teachers/" + teacher.getId() + "/add-course"))
            .andExpect(status().isOk());
        verify(teacherService).findById(teacher.getId());
        verify(courseService).findAll();
    }

    @Test
    void shouldShowDeletingACourse() throws Exception {
        Teacher teacher = buildTeacher();

        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(teacherService.getCourses(teacher)).thenReturn(new HashSet<>());
        mockMvc.perform(get("/teachers/" + teacher.getId() + "/delete-course"))
            .andExpect(status().isOk());
        verify(teacherService).findById(teacher.getId());
        verify(teacherService).getCourses(teacher);
    }

    @Test
    void shouldDeleteACourse() throws Exception {
        Teacher teacher = buildTeacher();
        Course course = buildCourse();

        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(courseService.findById(course.getId())).thenReturn(course);

        mockMvc.perform(delete("/teachers/delete-course")
                .param("teacherId", "" + teacher.getId())
                .param("courseId", "" + course.getId()))
            .andExpect(status().is3xxRedirection());
        verify(teacherService).removeCourse(teacher, course);
    }

    @Test
    void shouldAddCourses() throws Exception {
        Teacher teacher = buildTeacher();
        Course course = buildCourse();

        when(teacherService.findById(teacher.getId())).thenReturn(teacher);
        when(courseService.findById(course.getId())).thenReturn(course);

        mockMvc.perform(post("/teachers/add-course")
                .param("teacherId", "" + teacher.getId())
                .param("courseId", "" + course.getId()))
            .andExpect(status().is3xxRedirection());
        verify(teacherService).addToCourse(Mockito.any(), Mockito.any());
    }

    @Test
    void shouldBadRequestWhenSaveTeacherWithNameLengthMoreMaxLength() throws Exception {
        TeacherDto teacherDto = buildTeacherDto();
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, TEACHER_FIRST_NAME_MAX_LENGTH + 1).forEach(i -> builder.append('a'));
        teacherDto.setFirstName(builder.toString());
        assertThat(teacherDto.getFirstName().length(), greaterThan(TEACHER_FIRST_NAME_MAX_LENGTH));

        mockMvc.perform(post("/teachers")
                .flashAttr("teacher", teacherDto))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.firstName", Is.is(VALID_ERROR_TEACHER_FIRST_NAME_SIZE)));
    }

    @Test
    void shouldBadRequestWhenSaveTeacherWithBlankName() throws Exception {
        TeacherDto teacherDto = buildTeacherDto();
        teacherDto.setFirstName("");

        mockMvc.perform(post("/teachers")
                .flashAttr("teacher", teacherDto))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.firstName", Is.is(VALID_ERROR_TEACHER_FIRST_NAME_BLANK)));
    }

    private Course buildCourse() {
        Course testCourse = new Course();
        testCourse.setId(COURSE_ID);
        testCourse.setName(COURSE_NAME);
        return testCourse;
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
