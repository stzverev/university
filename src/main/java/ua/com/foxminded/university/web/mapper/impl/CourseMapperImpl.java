package ua.com.foxminded.university.web.mapper.impl;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.web.dto.CourseDto;

@Component
public class CourseMapperImpl extends GenericMapperAbstract<Course, CourseDto> {

    public CourseMapperImpl() {
        super(Course.class, CourseDto.class);
    }

}
