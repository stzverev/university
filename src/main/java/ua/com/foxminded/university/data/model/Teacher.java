package ua.com.foxminded.university.data.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "teachers")
@NamedQuery(name = "Teacher.getAll", query = "FROM Teacher")
@NamedQuery(name = "Teacher.getByFullName",
    query = "FROM Teacher WHERE firstName = :firstName AND lastName = :lastName")
@NamedQuery(name = "Teacher.getTabletime",
    query = "FROM Tabletime WHERE teacher = :teacher AND id.dateTime BETWEEN :begin AND :end")
public class Teacher extends Person {

    @ManyToMany
    @JoinTable(name = "teachers_courses",
        joinColumns = {@JoinColumn(name = "teacher_id")},
        inverseJoinColumns = {@JoinColumn(name = "course_id")})
    private Set<Course> courses;

    @OneToMany(mappedBy = "teacher")
    private Set<TabletimeRow> tabletime;

    public Teacher() {
        super();
    }

    public Set<Course> getCourses() {
        return courses;
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

}
