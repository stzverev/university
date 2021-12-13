package ua.com.foxminded.university.data.service.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.ConfigTest;
import ua.com.foxminded.university.data.model.Course;
import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.service.CourseService;
import ua.com.foxminded.university.data.service.GroupService;

@SpringJUnitConfig(ConfigTest.class)
@Sql(scripts = "classpath:data.sql")
class CourseServiceImplTest {

    private static final String COURSE_NAME = "Test course";
    private static final String GROUP_NAME = "Test group";

    @Autowired
    private CourseService courseService;

    @Autowired
    private GroupService groupService;

    @Test
    void shouldContainsGroupWhenAddedGroup() {
        Group group = new Group(GROUP_NAME);
        Course course = new Course(COURSE_NAME);
        groupService.save(group);
        courseService.save(course);
        courseService.addGroup(course, group);

        Set<Group> actual = courseService.getGroups(course);

        assertThat(actual, hasItem(group));
    }

}
