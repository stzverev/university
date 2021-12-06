package ua.com.foxminded.university.data.service;

import ua.com.foxminded.university.data.model.Student;

public interface StudentService extends CommonService<Student> {

    Student getByFullName(String firstName, String lastName);

}
