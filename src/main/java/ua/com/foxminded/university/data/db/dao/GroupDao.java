package ua.com.foxminded.university.data.db.dao;

import java.util.List;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;

public interface GroupDao extends GenericDao<Group> {

    Group getByName(String name);

    List<Student> getStudents(Group group);

}
