package ua.com.foxminded.university.web.mapper.impl.converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.web.dto.CourseDto;

@Component
public class CoursesToDtoListConverter extends AbstractConverter<Collection<Course>, List<CourseDto>> {

    @Override
    protected List<CourseDto> convert(Collection<Course> courses) {
        return courses.stream()
                .map(entity -> {
                    CourseDto dto = new CourseDto();
                    dto.setId(entity.getId());
                    dto.setName(entity.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
