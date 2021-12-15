package ua.com.foxminded.university.data.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "students")
public class Student extends Person {

    private static final long serialVersionUID = -2114857038032581600L;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student() {
        super();
    }

    public Student(String firstName, String lastName) {
        super(firstName, lastName);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Student [toString()="
                + super.toString() + ", group=" + group + "]";
    }

}
