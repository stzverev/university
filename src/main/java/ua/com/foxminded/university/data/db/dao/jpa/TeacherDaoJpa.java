package ua.com.foxminded.university.data.db.dao.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.db.dao.TeacherDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;

@Repository
@Transactional
public class TeacherDaoJpa extends AbstractJpaDao<Teacher> implements TeacherDao {

    private Logger logger = LoggerFactory.getLogger(TeacherDaoJpa.class);
    private static final String TEACHER_GET_BY_FULL_NAME = "Teacher.getByFullName";
    private static final String TEACHER_GET_TABLETIME = "Teacher.getTabletime";

    public TeacherDaoJpa() {
        super(Teacher.class);
    }

    @Override
    public Teacher getByFullName(String firstName, String lastName) {
        Teacher teacher = getEntityManager().createNamedQuery(TEACHER_GET_BY_FULL_NAME, Teacher.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getSingleResult();
        getEntityManager().detach(teacher);
        return teacher;
    }

    @Override
    public Set<Course> getCourses(Teacher teacher) {
        teacher = getEntityManager().find(Teacher.class, teacher.getId());
        logger.debug("teacher got: {}", teacher);
        Set<Course> courses = teacher.getCourses();
        logger.debug("courses got: {}", courses.size());
        getEntityManager().detach(teacher);
        courses.stream().forEach(getEntityManager()::detach);
        return courses;
    }

    @Override
    public void addToCourses(Teacher teacher, Set<Course> courses) {
        teacher = getEntityManager().find(Teacher.class, teacher.getId());
        courses.stream().forEach(teacher.getCourses()::add);
    }

    @Override
    public void removeCourse(Teacher teacher, Course course) {
        teacher = getEntityManager().find(Teacher.class, teacher.getId());
        teacher.getCourses().remove(course);
    }

    @Override
    public void addTabletimeRows(Set<TabletimeRow> rows) {
        rows.stream().forEach(getEntityManager()::persist);
    }

    @Override
    public void updateTabletime(Set<TabletimeRow> rows) {
        rows.stream().forEach(getEntityManager()::merge);
    }

    @Override
    public Set<TabletimeRow> getTabletime(Teacher teacher, LocalDateTime begin, LocalDateTime end) {
        List<TabletimeRow> tabletime = getEntityManager().createNamedQuery(TEACHER_GET_TABLETIME, TabletimeRow.class)
                .getResultList();
        tabletime.stream().forEach(getEntityManager()::detach);
        return tabletime.stream().collect(Collectors.toSet());
    }

}
