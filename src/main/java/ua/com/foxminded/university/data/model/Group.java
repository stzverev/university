package ua.com.foxminded.university.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
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
@Table(name = "groups")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Group extends AbstractEntity {

    private static final long serialVersionUID = 7265594806602299033L;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "groups_courses",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    @ToStringExclude
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "group")
    @ToStringExclude
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "group")
    @ToStringExclude
    private Set<TabletimeRow> tabletime;

    public Group(String name) {
        super();
        this.name = name;
    }

}
