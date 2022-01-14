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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Group extends AbstractEntity {

    private static final long serialVersionUID = 7265594806602299033L;

    @Column(name = "name")
    private @NonNull String name;

    @ManyToMany
    @JoinTable(name = "groups_courses",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    @ToString.Exclude
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "group")
    @ToString.Exclude
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "group")
    @ToString.Exclude
    private Set<TabletimeRow> tabletime;

}
