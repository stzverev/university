package ua.com.foxminded.university.data.model;

public class Student extends Person {

    private long id;
    private Group group;

    Student(String firstName, String lastName, Group group) {
        super(firstName, lastName);
        this.group = group;
    }

    public long getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Student [toString()="
                + super.toString() + ", group=" + group + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Student other = (Student) obj;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        return true;
    }

}
