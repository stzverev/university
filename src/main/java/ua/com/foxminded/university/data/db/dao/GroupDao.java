package ua.com.foxminded.university.data.db.dao;

import java.util.List;

import ua.com.foxminded.university.data.model.Group;
import ua.com.foxminded.university.data.model.Student;
import ua.com.foxminded.university.data.model.Tabletime;

public interface GroupDao extends GenericDao<Group> {

    Group getByName(String name);

    Tabletime getTabletime(Group group);

    List<Student> getStudents(Group group);

}
