package ua.com.foxminded.university.data.service;

import ua.com.foxminded.university.data.model.Student;

public interface StudentService extends CommonService<Student> {

    @Override
    Student getById(long id);

    Student getByFullName(String firstName, String lastName);



}
