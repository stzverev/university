package ua.com.foxminded.university.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
@NamedQuery(name = "Group.getByName", query = "FROM Group WHERE name = :name")
@NamedQuery(name = "Group.getAll", query = "FROM Group")
@NamedQuery(name = "Group.getTabletime",
    query = "FROM Tabletime WHERE group = :group AND id.dateTime BETWEEN :begin AND :end")
public class Group extends AbstractEntity {

    private static final long serialVersionUID = 7265594806602299033L;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "groups_courses",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<TabletimeRow> tabletime;

    public Group() {
        super();
    }

    public Group(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Set<TabletimeRow> getTabletime() {
        return tabletime;
    }

    public void setTabletime(Set<TabletimeRow> tabletime) {
        this.tabletime = tabletime;
    }

    @Override
    public String toString() {
        return "Group [name=" + name + ", id=" + getId() + "]";
    }

}
