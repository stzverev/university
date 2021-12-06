package ua.com.foxminded.university.data.db.dao.jpa;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityGraph;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.data.db.dao.GroupDao;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.model.TabletimeRow;

@Repository
@Transactional
public class GroupDaoJpa extends AbstractJpaDao<Group> implements GroupDao {

    private static final String JAVAX_PERSISTENCE_FETCHGRAPH = "javax.persistence.fetchgraph";
    private static final String GROUP_GET_TABLETIME = "Group.getTabletime";
    private static final String GROUP_GET_BY_NAME = "Group.getByName";

    private Logger logger = LoggerFactory.getLogger(GroupDaoJpa.class);

    public GroupDaoJpa() {
        super(Group.class);
    }

    @Override
    public Group getByName(String name) {
        Group group = getEntityManager().createNamedQuery(GROUP_GET_BY_NAME, Group.class)
                .setParameter("name", name)
                .getSingleResult();
        getEntityManager().detach(group);
        return group;
    }

    @Override
    public Set<Student> getStudents(Group group) {
        EntityGraph<Group> entityGraph = getEntityManager().createEntityGraph(Group.class);
        entityGraph.addAttributeNodes("students");
        HashMap<String, Object> properties = new HashMap<>();
        properties.put(JAVAX_PERSISTENCE_FETCHGRAPH, entityGraph);
        group = getEntityManager().find(Group.class, group.getId(), properties);
        return group.getStudents().stream()
                .collect(Collectors.toSet());
    }

    @Override
    public void addTabletimeRows(Set<TabletimeRow> rows) {
        rows.stream().forEach(row -> getEntityManager().persist(row));
    }

    @Override
    public void updateTabletime(Set<TabletimeRow> rows) {
        rows.stream().forEach(row -> getEntityManager().merge(row));
    }

    @Override
    public Set<TabletimeRow> getTabletime(Group group, LocalDateTime begin, LocalDateTime end) {
        List<TabletimeRow> tabletimeRows = getEntityManager()
                .createNamedQuery(GROUP_GET_TABLETIME, TabletimeRow.class)
                .setParameter("group", group)
                .setParameter("begin", begin)
                .setParameter("end", end)
                .getResultList();
        tabletimeRows.forEach(getEntityManager()::detach);
        return tabletimeRows.stream().collect(Collectors.toSet());
    }

    @Override
    public Set<Course> getCourses(Group group) {
        group = getEntityManager().find(Group.class, group.getId());
        Set<Course> courses = group.getCourses();
        logger.debug("courses got: {}", courses.size());
        getEntityManager().detach(group);
        logger.debug("group detached");
        courses.stream().forEach(getEntityManager()::detach);
        logger.debug("courses detached");
        return courses;
    }

    @Override
    public void addToCourses(Group group, Set<Course> courses) {
        group = getEntityManager().find(Group.class, group.getId());
        courses.stream().forEach(group.getCourses()::add);
    }

    @Override
    public void deleteFromCourse(Group group, Course course) {
        group = getEntityManager().find(Group.class, group.getId());
        group.getCourses().remove(course);
    }

}
