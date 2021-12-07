package ua.com.foxminded.university.data.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "courses")
@NamedQuery(name = "Course.getByName", query = "FROM Course WHERE name = :name")
@NamedQuery(name = "Course.getTabletime",
    query = "FROM Tabletime WHERE course = :course AND id.dateTime BETWEEN :begin AND :end")
@NamedQuery(name = "Course.getGroups",
    query = "From Course AS course JOIN FETCH course.groups WHERE course.id = :id")
public class Course extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "courses")
    private Set<Teacher> teachers;

    @ManyToMany(mappedBy = "courses")
    private Set<Group> groups;

    @OneToMany(mappedBy = "course")
    private Set<TabletimeRow> tabletime;

    public Course() {
        super();
    }

    public Set<TabletimeRow> getTabletime() {
        return tabletime;
    }

    public void setTabletime(Set<TabletimeRow> tabletime) {
        this.tabletime = tabletime;
    }

    public String getName() {
        return name;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Course [name=" + name + ", id=" + getId() + "]";
    }

}
