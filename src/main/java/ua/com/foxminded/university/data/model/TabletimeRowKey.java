package ua.com.foxminded.university.data.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TabletimeRowKey implements Serializable {

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "group_id")
    private long groupId;

    @Column(name = "course_id")
    private long courseId;

    @Column(name = "teacher_id")
    private long teacherId;

    public TabletimeRowKey() {
        super();
    }

    public TabletimeRowKey(LocalDateTime dateTime, long groupId, long courseId, long teacherId) {
        super();
        this.dateTime = dateTime;
        this.groupId = groupId;
        this.courseId = courseId;
        this.teacherId = teacherId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "TabletimeRowKey [dateTime=" + dateTime + ", groupId=" + groupId + ", courseId=" + courseId
                + ", teacherId=" + teacherId + "]";
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
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
        TabletimeRowKey other = (TabletimeRowKey) obj;
        return courseId == other.courseId && Objects.equals(dateTime, other.dateTime) && groupId == other.groupId
                && teacherId == other.teacherId;
    }

}
