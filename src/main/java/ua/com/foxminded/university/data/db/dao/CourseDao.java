package ua.com.foxminded.university.data.db.dao;

import ua.com.foxminded.university.data.model.Course;

public interface CourseDao extends GenericDao<Course> {

    Course getByName(String name);

}
