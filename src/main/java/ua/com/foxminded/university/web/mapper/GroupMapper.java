package ua.com.foxminded.university.web.mapper;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.web.dto.GroupDto;

public interface GroupMapper extends GenericMapper<Group, GroupDto> {

    Group toEntity(GroupDto dto);

}
