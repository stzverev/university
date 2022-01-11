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
import ua.com.foxminded.university.data.db.repository.TeacherRepository;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private static final Class<Teacher> ENTITY_CLASS = Teacher.class;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    @Override
    public void save(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    @Override
    public void save(List<Teacher> teachers) {
        teacherRepository.saveAll(teachers);
    }

    @Override
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher findById(long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(ENTITY_CLASS));
    }

    @Override
    public void addCourses(Teacher teacher, Set<Course> courses) {
        teacher = teacherRepository.getById(teacher.getId());
        Set<Long> coursesId = courses.stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        List<Course> foundCourses = courseRepository.findAllById(coursesId);
        foundCourses.stream().forEach(teacher.getCourses()::add);
    }

    @Override
    public void removeCourse(Teacher teacher, Course course) {
        teacher = teacherRepository.getById(teacher.getId());
        course = courseRepository.getById(course.getId());
        teacher.getCourses().remove(course);
    }

    @Override
    public Set<Course> getCourses(Teacher teacher) {
        return teacherRepository.findFetchCoursesById(teacher.getId())
                .orElseThrow(() -> new ObjectNotFoundException(teacher.getId(), ENTITY_CLASS))
                .getCourses();
    }

    @Override
    public void deleteById(long id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public void addToCourse(Teacher teacher, Course course) {
        teacher = teacherRepository.getById(teacher.getId());
        course = courseRepository.getById(course.getId());
        teacher.getCourses().add(course);
    }

    @Override
    public Page<Teacher> findAll(PageRequest pageRequest) {
        return teacherRepository.findAll(pageRequest);
    }

}
