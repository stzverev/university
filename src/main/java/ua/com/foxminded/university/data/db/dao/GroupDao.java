package ua.com.foxminded.university.data.db.dao;

import ua.com.foxminded.university.data.model.Group;

public interface GroupDao extends GenericDao<Group> {

    Group getByName(String name);

}
