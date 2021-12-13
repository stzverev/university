package ua.com.foxminded.university.data.db.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.com.foxminded.university.data.model.Group;

public interface GroupDao extends JpaRepository<Group, Long> {

    Group findByName(String name);

}
