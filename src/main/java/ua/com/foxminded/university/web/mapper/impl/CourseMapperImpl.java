package ua.com.foxminded.university.web.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.web.dto.CourseDto;
import ua.com.foxminded.university.web.mapper.CourseMapper;

@Component
public class CourseMapperImpl extends GenericMapperAbstract<Course, CourseDto>
    implements CourseMapper {

    @Autowired
    public CourseMapperImpl() {
        super(Course.class, CourseDto.class);
    }

}
