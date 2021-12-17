package ua.com.foxminded.university.web.mapper.impl;

import java.util.Collection;
import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.web.dto.CourseDto;
import ua.com.foxminded.university.web.mapper.CourseMapper;

@Component
public class CourseMapperImpl extends GenericMapperAbstract<Course, CourseDto>
    implements CourseMapper {

    @Autowired
    public CourseMapperImpl(Converter<Collection<Group>, List<String>> groupsToStringListCoverter) {
        super(Course.class, CourseDto.class);

        TypeMap<Course, CourseDto> typeMap = getModelMapper().createTypeMap(entityClass, dtoClass);
        typeMap.addMappings(mapper -> mapper.using(groupsToStringListCoverter)
                .map(Course::getGroups, CourseDto::setGroups));
    }

}
