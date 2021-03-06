package ua.com.foxminded.university.web.mapper.impl;

import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.web.dto.StudentDto;
import ua.com.foxminded.university.web.mapper.StudentMapper;

@Component
public class StudentMapperImpl extends GenericMapperAbstract<Student, StudentDto>
    implements StudentMapper {

    public StudentMapperImpl() {
        super(Student.class, StudentDto.class);

        TypeMap<StudentDto, Student> typeMap = getModelMapper()
            .createTypeMap(dtoClass, entityClass);

        typeMap.addMappings(mapper -> mapper.skip(Student::setId));
    }

    @Override
    public Student toEntity(StudentDto dto, Group group) {
        Student student = getModelMapper().map(dto, entityClass);
        student.setGroup(group);
        return student;
    }

}
