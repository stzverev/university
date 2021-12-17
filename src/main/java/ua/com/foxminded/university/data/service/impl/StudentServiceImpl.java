package ua.com.foxminded.university.data.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.university.data.db.repository.StudentRepository;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.StudentService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundException;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private static final Class<Student> ENTITY_CLASS = Student.class;
    private StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        super();
        this.studentRepository = studentRepository;
    }

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void save(List<Student> students) {
        studentRepository.saveAll(students);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student findById(long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, ENTITY_CLASS));
    }

    @Override
    public Student getByFullName(String firstName, String lastName) {
        return studentRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new ObjectNotFoundException(ENTITY_CLASS));
    }

    @Override
    public void deleteById(long id) {
        studentRepository.deleteById(id);
    }

}
