package ua.com.foxminded.university.web.mapper.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.Converter;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.web.dto.CourseDto;
import ua.com.foxminded.university.web.mapper.CourseMapper;
import ua.com.foxminded.university.web.mapper.impl.converter.GroupsToStringListCoverter;

class CourseMapperImplTest {

    private static final long COURSE_ID = 1L;
    private static final String COURSE_NAME = "test course";
    private static final String GROUP_NAME = "test group";

    private Converter<Collection<Group>, List<String>> groupsToStringListConverter = new GroupsToStringListCoverter();
    private CourseMapper courseMapper = new CourseMapperImpl(groupsToStringListConverter);

    @Test
    void shouldCorrectWhenConvertEntityToDto() {
        Group group = new Group(GROUP_NAME);

        Course entity = new Course(COURSE_NAME);
        entity.setId(COURSE_ID);
        entity.setGroups(Collections.singleton(group));

        CourseDto dto = courseMapper.toDto(entity);

        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getId(), dto.getId());
        assertThat(dto.getGroups(), hasItem(group.getName()));
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
