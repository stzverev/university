package ua.com.foxminded.university.web.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.StudentService;

@ExtendWith(MockitoExtension.class)
class GroupsControllerTest {

    @Mock
    private GroupService groupService;

    @Mock
    private CourseService courseService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private GroupsController groupController;

    private MockMvc mockMvc;
    private final static int GROUP_TEST_ID = 1;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void shouldGetGroupsListWhenGetGroups() throws Exception {
        mockMvc.perform(get("/groups"))
            .andExpect(status().isOk());
        verify(groupService).getAll();
    }

    @Test
    void shouldGetByIdWhenGetWithId() throws Exception {
        Group testGroup = new Group("test");
        List<Student> students = new ArrayList<>();
        when(groupService.getById(GROUP_TEST_ID)).thenReturn(testGroup);
        when(groupService.getStudents(testGroup)).thenReturn(students);
        mockMvc.perform(get("/groups/" + GROUP_TEST_ID + "/edit"))
            .andExpect(status().isOk());
        verify(groupService).getById(GROUP_TEST_ID);
    }

}
