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
        super();
    }

    public LocalDateTime getDateTime() {
        return id.getDateTime();
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

    public void setDateTime(LocalDateTime dateTime) {
        this.id.setDateTime(dateTime);
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
    public int hashCode() {
        return Objects.hash(course, group, id, teacher);
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
        return Objects.equals(course, other.course) && Objects.equals(group, other.group)
                && Objects.equals(id, other.id) && Objects.equals(teacher, other.teacher);
    }

}
