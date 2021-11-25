package ua.com.foxminded.university.data.service.beans;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.dao.StudentDao;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentDao studentDao;
    private final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

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

    @Override
    public void update(Student student) {
        studentDao.update(student);
    }

    @Override
    public void delete(long id) {
        studentDao.delete(id);
    }

}
