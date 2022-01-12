package ua.com.foxminded.university.data.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringExclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Teacher extends Person {

    private static final long serialVersionUID = 8935312762675961276L;

    @ManyToMany
    @JoinTable(name = "teachers_courses",
        joinColumns = {@JoinColumn(name = "teacher_id")},
        inverseJoinColumns = {@JoinColumn(name = "course_id")})
    @ToStringExclude
    private Set<Course> courses;

    @OneToMany(mappedBy = "teacher")
    @ToStringExclude
    private Set<TabletimeRow> tabletime;

    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
    }

}
