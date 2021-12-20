package ua.com.foxminded.university.web.mapper.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.web.dto.CourseDto;
import ua.com.foxminded.university.web.mapper.CourseMapper;

class CourseMapperImplTest {

    private static final String FIELD_NAME = "name";
    private static final long COURSE_ID = 1L;
    private static final String COURSE_NAME = "test course";
    private static final String GROUP_NAME = "test group";

    private CourseMapper courseMapper = new CourseMapperImpl();

    @Test
    void shouldCorrectWhenConvertEntityToDto() {
        Group group = new Group(GROUP_NAME);

        Course entity = new Course(COURSE_NAME);
        entity.setId(COURSE_ID);
        entity.setGroups(Collections.singleton(group));

        CourseDto dto = courseMapper.toDto(entity);

        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getId(), dto.getId());
        assertThat(dto.getGroups(), hasItem(hasProperty(FIELD_NAME, equalTo(group.getName()))));
    }

    @Test
    void shouldCorrectWhenConvertDtoToEntity() {
        CourseDto dto = new CourseDto();
        dto.setName(COURSE_NAME);
        dto.setId(COURSE_ID);

        Course entity = courseMapper.toEntity(dto);

        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getId(), entity.getId());
    }

}
