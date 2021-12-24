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

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.web.dto.CourseDto;
import ua.com.foxminded.university.web.exceptions.RestResponseEntityExceptionHandler;
import ua.com.foxminded.university.web.mapper.CourseMapper;

@ExtendWith(MockitoExtension.class)
class CoursesControllerTest {

    private static final int COURSE_NAME_MAX_LENGTH = 150;
    private static final String VALID_ERROR_COURSE_NAME_BLANK = "must not be blank";
    private static final String VALID_ERROR_COURSE_NAME_SIZE =
            "size must be between 0 and " + COURSE_NAME_MAX_LENGTH;
    private static final long COURSE_ID = 0;
    private static final String COURSE_NAME = "test";

    @Mock
    private CourseService courseService;

    @Mock
    private CourseMapper courseMapper;

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
        verify(courseService).findAll();
    }

    @Test
    void shouldGetByIdWhenGetWithId() throws Exception {
        when(courseService.findById(COURSE_ID)).thenReturn(buildCourse());
        mockMvc.perform(get("/courses/" + COURSE_ID + "/edit"))
            .andExpect(status().isOk());
        verify(courseService).findById(COURSE_ID);
    }

    @Test
    void shouldShowCreatingNew() throws Exception {
        mockMvc.perform(get("/courses/new"))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreateNew() throws Exception {
        Course course = buildCourse();
        CourseDto courseDto = buildCourseDto();
        when(courseMapper.toEntity(courseDto)).thenReturn(course);

        mockMvc.perform(post("/courses")
                .flashAttr("course", courseDto))
            .andExpect(status().is3xxRedirection());
        verify(courseService).save(course);
    }

    @Test
    void shouldUpdate() throws Exception {
        Course course = buildCourse();
        CourseDto courseDto = buildCourseDto();
        when(courseMapper.toEntity(courseDto)).thenReturn(course);

        mockMvc.perform(patch("/courses/" + COURSE_ID)
                .flashAttr("course", courseDto))
            .andExpect(status().is3xxRedirection());
        verify(courseService).save(course);
    }

    @Test
    void shouldDelete() throws Exception {
        mockMvc.perform(delete("/courses/" + COURSE_ID))
            .andExpect(status().is3xxRedirection());
        verify(courseService).deleteById(COURSE_ID);
    }

    @Test
    void shouldBadRequestWhenSaveCourseWithNameLengthMoreMaxLength() throws Exception {
        CourseDto courseDto = buildCourseDto();
        StringBuilder builder = new StringBuilder();
        IntStream.range(0, COURSE_NAME_MAX_LENGTH + 1).forEach(i -> builder.append('a'));
        courseDto.setName(builder.toString());
        assertThat(courseDto.getName().length(), greaterThan(COURSE_NAME_MAX_LENGTH));

        mockMvc.perform(post("/courses")
                .flashAttr("course", courseDto))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name", Is.is(VALID_ERROR_COURSE_NAME_SIZE)));
    }

    @Test
    void shouldBadRequestWhenSaveCourseWithBlankName() throws Exception {
        CourseDto courseDto = buildCourseDto();
        courseDto.setName("");

        mockMvc.perform(post("/courses")
                .flashAttr("course", courseDto))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.name", Is.is(VALID_ERROR_COURSE_NAME_BLANK)));
    }

    private Course buildCourse() {
        Course course = new Course();
        course.setId(COURSE_ID);
        course.setName(COURSE_NAME);
        return course;
    }

    private CourseDto buildCourseDto() {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(COURSE_ID);
        courseDto.setName(COURSE_NAME);
        return courseDto;
    }

}
