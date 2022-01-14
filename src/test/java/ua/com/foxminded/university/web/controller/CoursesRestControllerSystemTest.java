package ua.com.foxminded.university.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.university.web.dto.CourseDto;

@SpringBootTest
@AutoConfigureMockMvc
class CoursesRestControllerSystemTest {

    private static final int OFFSET = 0;
    private static final int LIMIT = 100;
    private static final String REQUEST_MAIN = "/courses-rest";
    private static final String COURSE_NAME = "test course";
    private static final long COURSE_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldGetAllCoursesWhenGetRequest() throws Exception {
        CourseDto courseDto = buildCourseDto();

        String json = mapper.writeValueAsString(courseDto);
        mockMvc.perform(post(REQUEST_MAIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());

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
        String json = mapper.writeValueAsString(courseDto);
        mockMvc.perform(post(REQUEST_MAIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());

        mockMvc.perform(get(REQUEST_MAIN + "/" + COURSE_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Is.is((int) COURSE_ID)))
            .andExpect(jsonPath("$.name", Is.is(COURSE_NAME)));
    }

    @Test
    void shouldIsOkWhenPatchRequest() throws Exception {
        CourseDto courseDto = buildCourseDto();

        String json = mapper.writeValueAsString(courseDto);
        mockMvc.perform(patch(REQUEST_MAIN)
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

}
