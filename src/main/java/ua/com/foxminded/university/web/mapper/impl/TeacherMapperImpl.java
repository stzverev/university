package ua.com.foxminded.university.web.mapper.impl;

import java.util.Collection;
import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.web.dto.CourseDto;
import ua.com.foxminded.university.web.dto.TeacherDto;
import ua.com.foxminded.university.web.mapper.TeacherMapper;

@Component
public class TeacherMapperImpl extends GenericMapperAbstract<Teacher, TeacherDto>
        implements TeacherMapper {

    public TeacherMapperImpl(Converter<Collection<Course>, List<CourseDto>> coursesToStringConverter) {
        super(Teacher.class, TeacherDto.class);
        TypeMap<Teacher, TeacherDto> typeMap = getModelMapper().createTypeMap(entityClass, dtoClass);

        typeMap.addMappings(mapper -> mapper.using(coursesToStringConverter)
                .map(Teacher::getCourses, TeacherDto::setCourses));
    }

}
