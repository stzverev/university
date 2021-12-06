package ua.com.foxminded.university.data.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TimetableRowId implements Serializable {

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "teacher_id")
    private Long teacherId;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, dateTime, groupId, teacherId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TimetableRowId other = (TimetableRowId) obj;
        return Objects.equals(courseId, other.courseId) && Objects.equals(dateTime, other.dateTime)
                && Objects.equals(groupId, other.groupId) && Objects.equals(teacherId, other.teacherId);
    }

}
