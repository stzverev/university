package ua.com.foxminded.university.data.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "Tabletime")
@Table(name = "tabletime")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TabletimeRow extends AbstractEntity {

    @Column(name = "date_time")
    @NotNull(message = "Date/time is mandatory")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @NotNull(message = "Group is mandatory")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @NotNull(message = "Course is mandatory")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @NotNull(message = "Teacher is mandatory")
    private Teacher teacher;

    public TabletimeRow(LocalDateTime dateTime, Course course, Group group, Teacher teacher) {
        this.dateTime = dateTime;
        this.course = course;
        this.group = group;
        this.teacher = teacher;
    }

}
