package ua.com.foxminded.university.web.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.web.dto.GroupDto;
import ua.com.foxminded.university.web.mapper.GroupMapper;

@Component
public class GroupMapperImpl extends GenericMapperAbstract<Group, GroupDto>
        implements GroupMapper {

    @Autowired
    public GroupMapperImpl() {
        super(Group.class, GroupDto.class);
    }

    @Override
    public Group toEntity(GroupDto dto) {
        return getModelMapper().map(dto, entityClass);
    }

}
