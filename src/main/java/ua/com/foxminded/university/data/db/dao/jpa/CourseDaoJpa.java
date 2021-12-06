package ua.com.foxminded.university.data.db.dao.jpa;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityGraph;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.db.dao.CourseDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;

@Repository
@Transactional
public class CourseDaoJpa extends AbstractJpaDao<Course> implements CourseDao {

    private static final String JAVAX_PERSISTENCE_FETCHGRAPH = "javax.persistence.fetchgraph";
    private static final String COURSE_GET_GROUPS = "Course.getGroups";
    private static final String COURSE_GET_TABLETIME = "Course.getTabletime";
    private static final String COURSE_GET_BY_NAME = "Course.getByName";

    public CourseDaoJpa() {
        super(Course.class);
    }

    @Override
    public Course getByName(String name) {
        Course course = getEntityManager().createNamedQuery(COURSE_GET_BY_NAME, Course.class)
            .setParameter("name", name)
            .getSingleResult();
        getEntityManager().detach(course);
        return course;
    }

    @Override
    public void saveTabletime(List<TabletimeRow> rows) {
        rows.stream().forEach(getEntityManager()::persist);
    }

    @Override
    public void updateTabletime(List<TabletimeRow> rows) {
        rows.stream().forEach(getEntityManager()::merge);
    }

    @Override
    public List<TabletimeRow> getTabletime(Course course, LocalDateTime begin, LocalDateTime end) {
        List<TabletimeRow> tabletime = getEntityManager()
                .createNamedQuery(COURSE_GET_TABLETIME, TabletimeRow.class)
                .setParameter("course", course)
                .setParameter("begin", begin)
                .setParameter("end", end)
                .getResultList();
        tabletime.stream().forEach(getEntityManager()::detach);
        return tabletime;
    }

    @Override
    public Set<Teacher> getTeachers(Course course) {
        EntityGraph<Course> entityGraph = getEntityManager().createEntityGraph(Course.class);
        entityGraph.addAttributeNodes("teachers");
        Map<String, Object> properties = new HashMap<>();
        properties.put(JAVAX_PERSISTENCE_FETCHGRAPH, entityGraph);
        course = getEntityManager().find(Course.class, course.getId(), properties);
        return course.getTeachers();
    }

    @Override
    public Set<Group> getGroups(Course course) {
        course = getEntityManager().createNamedQuery(COURSE_GET_GROUPS, Course.class)
                .setParameter("id", course.getId())
                .getSingleResult();
        Set<Group> groups = course.getGroups().stream()
                .collect(Collectors.toSet());
        groups.stream().forEach(getEntityManager()::detach);
        return groups;
    }

}
