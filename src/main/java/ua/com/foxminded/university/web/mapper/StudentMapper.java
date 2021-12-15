package ua.com.foxminded.university.web.mapper;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.web.dto.StudentDto;

public interface StudentMapper extends GenericMapper<Student, StudentDto> {

    Student toEntity(StudentDto dto, Group group);

}
