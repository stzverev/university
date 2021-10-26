package ua.com.foxminded.university.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.StudentDao;
import ua.com.foxminded.university.data.model.Student;

@Service
public class StudentServiceImpl implements StudentService {

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
        return studentDao.getById(id);
    }

    @Override
    public Student getByFullName(String firstName, String lastName) {
        return studentDao.getByFullName(firstName, lastName);
    }

}
