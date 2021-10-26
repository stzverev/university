package ua.com.foxminded.university.data.service;

import ua.com.foxminded.university.data.model.Student;

interface StudentService extends CommonService<Student> {

    Student getById(long id);

    Student getByFullName(String firstName, String lastName);

}
