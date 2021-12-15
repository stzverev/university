package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TabletimeDto implements Serializable {

    private static final long serialVersionUID = -6001766765168537465L;

    private Long id;
    private LocalDateTime dateTime;
    private String group;
    private String course;
    private String teacher;

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public TabletimeDto() {
        super();
    }

}
