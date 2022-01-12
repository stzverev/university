package ua.com.foxminded.university.data.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.data.db.repository.CourseRepository;
import ua.com.foxminded.university.data.db.repository.GroupRepository;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private static final Class<Group> ENTITY_CLASS = Group.class;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;

    @Override
    public void save(Group group) {
        groupRepository.save(group);
    }

    @Override
    public void save(List<Group> groups) {
        groupRepository.saveAll(groups);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group findById(long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, ENTITY_CLASS));
    }

    @Override
    public void addToCourses(Group group, Set<Course> courses) {
        long id = group.getId();
        group = groupRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, ENTITY_CLASS));
        Set<Long> coursesId = courses.stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        courseRepository.findAllById(coursesId)
            .stream()
            .forEach(group.getCourses()::add);
    }

    @Override
    public void removeFromCourse(Group group, Course course) {
        long id = group.getId();
        group = groupRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, ENTITY_CLASS));
        course = courseRepository.getById(course.getId());
        group.getCourses().remove(course);
    }

    @Override
    public Set<Student> findStudents(Group group) {
        return groupRepository.findFetchStudentsById(group.getId())
                .orElseThrow(() -> new ObjectNotFoundException(group.getId(), ENTITY_CLASS))
                .getStudents();
    }

    @Override
    public void deleteById(long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public Set<Course> findCourses(Group group) {
        return groupRepository.findFetchCoursesById(group.getId())
                .orElseThrow(() -> new ObjectNotFoundException(group.getId(), ENTITY_CLASS))
                .getCourses();
    }

    @Override
    public void addToCourse(Group group, Course course) {
        long id = group.getId();
        group = groupRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id, ENTITY_CLASS));
        long courseId = course.getId();
        course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ObjectNotFoundException(courseId, Course.class));
        group.getCourses().add(course);
    }

    @Override
    public Group findWithCoursesById(long groupId) {
        return groupRepository.findFetchCoursesById(groupId)
                .orElseThrow(() -> new ObjectNotFoundException(groupId, ENTITY_CLASS));
    }

    @Override
    public Page<Group> findAll(PageRequest pageRequest) {
        return groupRepository.findAll(pageRequest);
    }

}
