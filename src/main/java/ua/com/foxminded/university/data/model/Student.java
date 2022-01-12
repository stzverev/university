package ua.com.foxminded.university.data.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "students")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Student extends Person {

    private static final long serialVersionUID = -2114857038032581600L;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student(String firstName, String lastName) {
        super(firstName, lastName);
    }

}
