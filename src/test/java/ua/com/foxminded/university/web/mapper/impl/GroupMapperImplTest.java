package ua.com.foxminded.university.web.mapper.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.web.dto.GroupDto;
import ua.com.foxminded.university.web.mapper.impl.converter.CoursesToStringListConverter;

class GroupMapperImplTest {

    private static final String COURSE_NAME = "test course";
    private static final long GROUP_ID = 1L;
    private static final String GROUP_NAME = "test group";
    private CoursesToStringListConverter courseListConverter = new CoursesToStringListConverter();
    private GroupMapperImpl groupMapper = new GroupMapperImpl(courseListConverter);

    @Test
    void shouldCorrectWhenConvertDtoToEntity() {
        GroupDto dto = new GroupDto();
        dto.setName(GROUP_NAME);
        dto.setId(GROUP_ID);

        Group entity = groupMapper.toEntity(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getName(), entity.getName());
    }

    @Test
    void shouldCorrectWhenConvertEntityToDto() {
        Course course = new Course(COURSE_NAME);

        Group entity = new Group(GROUP_NAME);
        entity.setId(GROUP_ID);
        entity.setCourses(Collections.singleton(course));

        GroupDto dto = groupMapper.toDto(entity);

        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getId(), dto.getId());
        assertThat(dto.getCourses(), hasItem(course.getName()));
    }

}
