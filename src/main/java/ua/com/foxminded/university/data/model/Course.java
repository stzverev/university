package ua.com.foxminded.university.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Course extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "courses")
    private Set<Teacher> teachers = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    @ToString.Exclude
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "course")
    @ToString.Exclude
    private Set<TabletimeRow> tabletime = new HashSet<>();

    public Course(String name) {
        super();
        this.name = name;
    }

}
