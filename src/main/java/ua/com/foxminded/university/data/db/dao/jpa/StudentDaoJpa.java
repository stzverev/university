package ua.com.foxminded.university.data.db.dao.jpa;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.db.dao.StudentDao;
import ua.com.foxminded.university.data.model.Student;

@Repository
@Transactional
public class StudentDaoJpa extends AbstractJpaDao<Student> implements StudentDao {

    private static final String STUDENT_GET_BY_FULL_NAME = "Student.getByFullName";

    public StudentDaoJpa() {
        super(Student.class);
    }

    @Override
    public Student getByFullName(String firstName, String lastName) {
        Student student = getEntityManager()
                .createNamedQuery(STUDENT_GET_BY_FULL_NAME, Student.class)
                .setParameter("lastName", lastName)
                .setParameter("firstName", firstName)
                .getSingleResult();
        getEntityManager().detach(student);
        return student;
    }

}
