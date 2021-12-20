package ua.com.foxminded.university.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "courses")
@NamedQuery(name = "Course.getTabletime",
    query = "FROM Tabletime WHERE course = :course AND id.dateTime BETWEEN :begin AND :end")
public class Course extends AbstractEntity {

    @Column(name = "name")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @ManyToMany(mappedBy = "courses")
    private Set<Teacher> teachers = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<TabletimeRow> tabletime = new HashSet<>();

    public Course(String name) {
        super();
        this.name = name;
    }

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
