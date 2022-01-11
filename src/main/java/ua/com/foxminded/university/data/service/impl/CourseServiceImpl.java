package ua.com.foxminded.university.data.service.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.data.db.repository.CourseRepository;
import ua.com.foxminded.university.data.db.repository.GroupRepository;
import ua.com.foxminded.university.data.db.repository.TeacherRepository;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Class<Course> ENTITY_CLASS = Course.class;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;

    @Override
    public void save(Course course) {
        courseRepository.save(course);
    }

    @Override
    public void save(List<Course> courses) {
        courseRepository.saveAll(courses);
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public Course findById(long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, ENTITY_CLASS));
    }

    @Override
    public Set<Teacher> getTeachers(Course course) {
        return courseRepository.findFetchTeachersById(course.getId())
                .orElseThrow(() -> new ObjectNotFoundException(course.getId(), ENTITY_CLASS))
                .getTeachers();
    }

    @Override
    public Set<Group> getGroups(Course course) {
        return courseRepository.findFetchGroupsById(course.getId())
                .orElseThrow(() -> new ObjectNotFoundException(course.getId(), ENTITY_CLASS))
                .getGroups();
    }

    @Override
    public void addGroup(Course course, Group group) {
        course = courseRepository.getById(course.getId());
        group = groupRepository.getById(group.getId());
        group.getCourses().add(course);
    }

    @Override
    public void removeGroupFromCourse(Course course, Group group) {
        course = courseRepository.getById(course.getId());
        group = groupRepository.getById(group.getId());
        group.getCourses().remove(course);
    }

    @Override
    public void addTeacher(Course course, Teacher teacher) {
        course = courseRepository.getById(course.getId());
        teacher = teacherRepository.getById(teacher.getId());
        teacher.getCourses().add(course);
    }

    @Override
    public void removeTeacherFromCourse(Course course, Teacher teacher) {
        course = courseRepository.getById(course.getId());
        teacher = teacherRepository.getById(teacher.getId());
        teacher.getCourses().remove(course);
    }

    @Override
    public void deleteById(long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public Page<Course> findAll(PageRequest pageRequest) {
        return courseRepository.findAll(pageRequest);
    }

}
