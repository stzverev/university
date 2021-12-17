package ua.com.foxminded.university.web.mapper.impl;

import java.util.Collection;
import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.web.dto.GroupDto;
import ua.com.foxminded.university.web.mapper.GroupMapper;

@Component
public class GroupMapperImpl extends GenericMapperAbstract<Group, GroupDto>
        implements GroupMapper {

    @Autowired
    public GroupMapperImpl(Converter<Collection<Course>, List<String>> courseListToStringListConverter) {
        super(Group.class, GroupDto.class);
        TypeMap<Group, GroupDto> typeMap = getModelMapper()
                .createTypeMap(entityClass, dtoClass);

        typeMap.addMappings(mapper -> mapper.using(courseListToStringListConverter)
            .map(Group::getCourses, GroupDto::setCourses));
    }

    @Override
    public Group toEntity(GroupDto dto) {
        return getModelMapper().map(dto, entityClass);
    }

}
