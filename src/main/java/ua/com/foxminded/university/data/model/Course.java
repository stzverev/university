package ua.com.foxminded.university.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringExclude;

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
    @ToStringExclude
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "course")
    @ToStringExclude
    private Set<TabletimeRow> tabletime = new HashSet<>();

    public Course(String name) {
        super();
        this.name = name;
    }

}
