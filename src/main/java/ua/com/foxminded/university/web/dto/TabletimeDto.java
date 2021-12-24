package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class TabletimeDto implements Serializable {

    private static final long serialVersionUID = -6001766765168537465L;

    private Long id;

    private LocalDateTime dateTime;
    private String groupName;
    private String courseName;
    private String teacherFirstName;
    private String teacherLastName;

    public TabletimeDto() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherFirstName() {
        return teacherFirstName;
    }

    public void setTeacherFirstName(String teacherFirstName) {
        this.teacherFirstName = teacherFirstName;
    }

    public String getTeacherLastName() {
        return teacherLastName;
    }

    public void setTeacherLastName(String teacherLastName) {
        this.teacherLastName = teacherLastName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseName, dateTime, groupName, id, teacherFirstName, teacherLastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TabletimeDto other = (TabletimeDto) obj;
        return Objects.equals(courseName, other.courseName) && Objects.equals(dateTime, other.dateTime)
                && Objects.equals(groupName, other.groupName) && Objects.equals(id, other.id)
                && Objects.equals(teacherFirstName, other.teacherFirstName)
                && Objects.equals(teacherLastName, other.teacherLastName);
    }

}
