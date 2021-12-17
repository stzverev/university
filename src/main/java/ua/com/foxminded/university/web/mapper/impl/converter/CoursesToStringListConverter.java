package ua.com.foxminded.university.web.mapper.impl.converter;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Course;

@Component
public class CoursesToStringListConverter extends AbstractConverter<Collection<Course>, List<String>> {

    @Override
    protected List<String> convert(Collection<Course> courses) {
        return courses.stream()
                .map(Course::getName)
                .collect(Collectors.toList());
    }

}
