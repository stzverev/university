package ua.com.foxminded.university.data.service.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.TabletimeRow;
import ua.com.foxminded.university.data.model.Teacher;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.GroupService;
import ua.com.foxminded.university.data.service.TabletimeService;
import ua.com.foxminded.university.data.service.TeacherService;
import ua.com.foxminded.university.exceptions.ObjectNotFoundException;

@SpringBootTest
class TabletimeServiceImplTest {

    private static final String TEACHER_LAST_NAME = "Teacher";
    private static final String TEACHER_FIRST_NAME = "Test";
    private static final String GROUP_NAME = "Test group";
    private static final String COURSE_NAME = "Test course";
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2021, 12, 15, 8, 0);
    private static final LocalDateTime BEGIN = LocalDateTime.of(2021, 12, 1, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2021, 12, 31, 23, 59);

    @Autowired
    private TabletimeService tabletimeService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private GroupService groupService;

    @Test
    void shouldGetTabletimeAfteraAdding() {
        Course course = new Course(COURSE_NAME);
        courseService.save(course);

        Group group = new Group(GROUP_NAME);
        groupService.save(group);

        Teacher teacher = new Teacher(TEACHER_FIRST_NAME, TEACHER_LAST_NAME);
        teacherService.save(teacher);

        TabletimeRow tabletimeRow = new TabletimeRow(DATE_TIME, course, group, teacher);
        tabletimeService.save(tabletimeRow);

        List<TabletimeRow> actual = tabletimeService.findAll();
        assertThat(actual, hasItem(tabletimeRow));

        List<TabletimeRow> actualForCourse = tabletimeService.getTabletimeForCourse(course.getId(), BEGIN, END);
        assertThat(actualForCourse, hasItem(tabletimeRow));

        List<TabletimeRow> actualForTeacher = tabletimeService.getTabletimeForTeacher(teacher.getId(), BEGIN, END);
        assertThat(actualForTeacher, hasItem(tabletimeRow));

        List<TabletimeRow> actualForGroup = tabletimeService.getTabletimeForGroup(group.getId(), BEGIN, END);
        assertThat(actualForGroup, hasItem(tabletimeRow));
    }

    @Test
    void shouldThrowExceptionWhenGetTabletimeByIdAfterDeleting() {
        Course course = new Course(COURSE_NAME);
        courseService.save(course);

        Group group = new Group(GROUP_NAME);
        groupService.save(group);

        Teacher teacher = new Teacher(TEACHER_FIRST_NAME, TEACHER_LAST_NAME);
        teacherService.save(teacher);

        TabletimeRow tabletimeRow = new TabletimeRow(DATE_TIME, course, group, teacher);
        tabletimeService.save(tabletimeRow);

        assertEquals(tabletimeRow, tabletimeService.findById(tabletimeRow.getId()));

        long tabletimeId = tabletimeRow.getId();
        tabletimeService.deleteById(tabletimeRow.getId());
        ObjectNotFoundException e = assertThrows(ObjectNotFoundException.class,
                () -> tabletimeService.findById(tabletimeId));
        assertEquals("ua.com.foxminded.university.data.model.TabletimeRow not found by id: " + tabletimeId, e.getMessage());
    }

}
