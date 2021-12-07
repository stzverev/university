package ua.com.foxminded.university.data.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity(name = "Tabletime")
@Table(name = "tabletime")
public class TabletimeRow implements Serializable {

    @EmbeddedId
    private TabletimeRowKey id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @MapsId("teacherId")
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public TabletimeRow() {
    }

    public TabletimeRow(LocalDateTime dateTime, Course course, Group group, Teacher teacher) {
        this.id = new TabletimeRowKey(dateTime, group.getId(), course.getId(), teacher.getId());
        this.course = course;
        this.group = group;
        this.teacher = teacher;
    }

    public void setId(TabletimeRowKey id) {
        this.id = id;
    }

    public TabletimeRowKey getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public Course getCourse() {
        return course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "TabletimeRow [id=" + id + ", group=" + group + ", course=" + course + ", teacher=" + teacher + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TabletimeRow other = (TabletimeRow) obj;
        return Objects.equals(id, other.id);
    }

}
