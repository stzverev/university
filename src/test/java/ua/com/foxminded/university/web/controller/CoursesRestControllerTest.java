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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.web.dto.CourseDto;
import ua.com.foxminded.university.web.exceptions.RestResponseEntityExceptionHandler;
import ua.com.foxminded.university.web.mapper.CourseMapper;

@ExtendWith(MockitoExtension.class)
class CoursesRestControllerTest {

    private static final int OFFSET = 100;
    private static final int LIMIT = 100;
    private static final String REQUEST_MAIN = "/courses-rest";
    private static final String COURSE_NAME = "test course";
    private static final long COURSE_ID = 1L;

    @Mock
    private CourseService courseService;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseRestController courseRestController;

    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private RestResponseEntityExceptionHandler controllerAdvice = new RestResponseEntityExceptionHandler();

    @BeforeEach
    private void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseRestController)
                .setControllerAdvice(controllerAdvice)
                .build();
    }

    @Test
    void shouldGetAllCoursesWhenGetRequest() throws Exception {
        CourseDto courseDto = buildCourseDto();
        Course course = buildCourse();
        Page<Course> page = new PageImpl<>(Collections.singletonList(course));

        when(courseService.findAll(Mockito.any(PageRequest.class))).thenReturn(page);
        when(courseMapper.toDto(course)).thenReturn(courseDto);

        mockMvc.perform(get(REQUEST_MAIN)
                .param("limit", "" + LIMIT)
                .param("offset", "" + OFFSET))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", Is.is((int) COURSE_ID)))
            .andExpect(jsonPath("$[0].name", Is.is(COURSE_NAME)));
    }

    @Test
    void shouldGetCourseWhenGetRequest() throws Exception {
        CourseDto courseDto = buildCourseDto();
        Course course = buildCourse();

        when(courseService.findById(COURSE_ID)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(courseDto);

        mockMvc.perform(get(REQUEST_MAIN + "/" + COURSE_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Is.is((int) COURSE_ID)))
            .andExpect(jsonPath("$.name", Is.is(COURSE_NAME)));
    }

    @Test
    void shouldIsOkWhenPatchRequest() throws Exception {
        Course course = buildCourse();
        CourseDto courseDto = buildCourseDto();

        when(courseMapper.toEntity(courseDto)).thenReturn(course);

        String json = mapper.writeValueAsString(courseDto);
        mockMvc.perform(patch(REQUEST_MAIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    void shouldIsOkWhenPostRequest() throws Exception {
        Course course = buildCourse();
        CourseDto courseDto = buildCourseDto();

        when(courseMapper.toEntity(courseDto)).thenReturn(course);

        String json = mapper.writeValueAsString(courseDto);
        mockMvc.perform(post(REQUEST_MAIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    void shoudIsOkWhenDeleteRequest() throws Exception {
        mockMvc.perform(delete(REQUEST_MAIN)
                .param("id", "" + COURSE_ID))
            .andExpect(status().isOk());
    }

    private CourseDto buildCourseDto() {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(COURSE_ID);
        courseDto.setName(COURSE_NAME);
        return courseDto;
    }

    private Course buildCourse() {
        Course course = new Course();
        course.setId(COURSE_ID);
        course.setName(COURSE_NAME);
        return course;
    }

}
