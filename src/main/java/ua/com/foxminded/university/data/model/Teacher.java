package ua.com.foxminded.university.data.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "teachers")
public class Teacher extends Person {

    private static final long serialVersionUID = 8935312762675961276L;

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

    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
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
