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

import ua.com.foxminded.university.web.dto.GroupDto;

@SpringBootTest
@AutoConfigureMockMvc
class GroupsRestControllerSystemTest {

    private static final int OFFSET = 0;
    private static final int LIMIT = 100;
    private static final String REQUEST_MAIN = "/groups-rest";
    private static final String GROUP_NAME = "test group";
    private static final long GROUP_ID = 1L;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetAllGroupsWhenGetRequest() throws Exception {
        GroupDto groupDto = buildGroupDto();
        String json = mapper.writeValueAsString(groupDto);
        mockMvc.perform(post(REQUEST_MAIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());

        mockMvc.perform(get(REQUEST_MAIN)
                .param("limit", "" + LIMIT)
                .param("offset", "" + OFFSET))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id", Is.is((int) GROUP_ID)))
            .andExpect(jsonPath("$[0].name", Is.is(GROUP_NAME)));
    }

    @Test
    void shouldGetGroupWhenGetRequest() throws Exception {
        GroupDto groupDto = buildGroupDto();
        String json = mapper.writeValueAsString(groupDto);
        mockMvc.perform(post(REQUEST_MAIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());

        mockMvc.perform(get(REQUEST_MAIN + "/" + GROUP_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Is.is((int) GROUP_ID)))
            .andExpect(jsonPath("$.name", Is.is(GROUP_NAME)));
    }

    @Test
    void shouldIsOkWhenPatchRequest() throws Exception {
        GroupDto groupDto = buildGroupDto();

        String json = mapper.writeValueAsString(groupDto);
        mockMvc.perform(patch(REQUEST_MAIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }

    @Test
    void shoudIsOkWhenDeleteRequest() throws Exception {
        mockMvc.perform(delete(REQUEST_MAIN)
                .param("id", "" + GROUP_ID))
            .andExpect(status().isOk());
    }

    private GroupDto buildGroupDto() {
        GroupDto groupDto = new GroupDto();
        groupDto.setId(GROUP_ID);
        groupDto.setName(GROUP_NAME);
        return groupDto;
    }

}
