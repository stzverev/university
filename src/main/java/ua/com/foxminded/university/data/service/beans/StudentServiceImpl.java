package ua.com.foxminded.university.data.service.beans;

import java.util.List;
import java.util.stream.Collectors;

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
    private final Logger logger = LoggerFactory.getLogger(
            StudentServiceImpl.class);

    @Autowired
    public StudentServiceImpl(StudentDao studentDao) {
        super();
        this.studentDao = studentDao;
    }

    @Override
    public void save(Student student) {
        logger.debug("The saving of student has started: {}", student);
        try {
            studentDao.save(student);
        } catch (Exception e) {
            logger.error(""
                    + "Error when saving the student:%n student: {};%n"
                    + "error: {}", student, e.getMessage());
        }
    }

    @Override
    public void save(List<Student> students) {
        String studentsDescription = students
                .stream()
                .map(Student::toString)
                .collect(Collectors.joining("; " + System.lineSeparator()));
        logger.debug("The saving of list students has started:%n {}",
                studentsDescription);
        try {
            studentDao.save(students);
        } catch (Exception e) {
            logger.error(""
                    + "Error when saving list students:%n students:{}%n"
                    + "error: {}", studentsDescription, e.getMessage());
        }
    }

    @Override
    public List<Student> getAll() {
        logger.debug("The getting of all students has started");
        return studentDao.getAll();
    }

    @Override
    public Student getById(long id) {
        logger.debug("The getting of student by id has started:%n id: {}", id);
        return studentDao.getById(id);
    }

    @Override
    public Student getByFullName(String firstName, String lastName) {
        logger.debug(""
                + "The getting of student by full name has started:%n"
                + "first name: {};%n"
                + "last name: {}", firstName, lastName);
        return studentDao.getByFullName(firstName, lastName);
    }

}
