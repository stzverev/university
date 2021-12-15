package ua.com.foxminded.university.web.mapper.impl;

import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.web.dto.TeacherDto;
import ua.com.foxminded.university.web.mapper.TeacherMapper;

@Component
public class TeacherMapperImpl extends GenericMapperAbstract<Teacher, TeacherDto>
        implements TeacherMapper {

    public TeacherMapperImpl() {
        super(Teacher.class, TeacherDto.class);
    }

}
