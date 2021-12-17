package ua.com.foxminded.university.web.mapper.impl.converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Group;

@Component
public class GroupsToStringListCoverter extends AbstractConverter<Collection<Group>, List<String>> {

    @Override
    protected List<String> convert(Collection<Group> groups) {
        return groups.stream()
                .map(Group::getName)
                .collect(Collectors.toList());
    }

}
