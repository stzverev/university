package ua.com.foxminded.university.web.mapper.impl;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.web.dto.TabletimeDto;

@Component
public class TabletimeMapperImpl extends GenericMapperAbstract<TabletimeRow, TabletimeDto> {

    public TabletimeMapperImpl() {
        super(TabletimeRow.class, TabletimeDto.class);
    }

}
