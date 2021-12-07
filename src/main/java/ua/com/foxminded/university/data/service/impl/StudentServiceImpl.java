package ua.com.foxminded.university.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.StudentDao;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.StudentService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundById;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Class<Student> ENTITY_CLASS = Student.class;
    private StudentDao studentDao;

    @Autowired
    public StudentServiceImpl(StudentDao studentDao) {
        super();
        this.studentDao = studentDao;
    }

    @Override
    public void save(Student student) {
        studentDao.save(student);
    }

    @Override
    public void save(List<Student> students) {
        studentDao.save(students);
    }

    @Override
    public List<Student> getAll() {
        return studentDao.getAll();
    }

    @Override
    public Student getById(long id) {
        return studentDao.getById(id).orElseThrow(() -> new ObjectNotFoundById(id, ENTITY_CLASS));
    }

    @Override
    public Student getByFullName(String firstName, String lastName) {
        return studentDao.getByFullName(firstName, lastName);
    }

    @Override
    public void update(Student student) {
        studentDao.update(student);
    }

    @Override
    public void delete(long id) {
        studentDao.delete(id);
    }

}
