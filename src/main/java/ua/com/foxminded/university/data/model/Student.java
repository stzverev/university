package ua.com.foxminded.university.data.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "students")
@NamedQuery(name = "Student.getByFullName",
    query = "FROM Student WHERE firstName = :firstName AND lastName = :lastName")
public class Student extends Person {

    @ManyToOne
    private Group group;

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
