package ua.com.foxminded.university.data.model;

import java.time.LocalDateTime;

public class TabletimeRow {

    private LocalDateTime dateTime;
    private Group group;
    private Course course;
    private Teacher teacher;

    public LocalDateTime getDateTime() {
        return dateTime;
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
        this.dateTime = dateTime;
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
        return "TabletimeRow [dateTime="
                + dateTime
                + ", group="
                + group
                + ", course="
                + course
                + ", teacher="
                + teacher
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((course == null) ? 0 : course.hashCode());
        result = prime * result + ((dateTime == null) ? 0
                : dateTime.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
        return result;
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
        if (course == null) {
            if (other.course != null)
                return false;
        } else if (!course.equals(other.course))
            return false;
        if (dateTime == null) {
            if (other.dateTime != null)
                return false;
        } else if (!dateTime.equals(other.dateTime))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (teacher == null) {
            if (other.teacher != null)
                return false;
        } else if (!teacher.equals(other.teacher))
            return false;
        return true;
    }

}
