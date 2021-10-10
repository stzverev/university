package ua.com.foxminded.university.data.model;

import java.util.List;

public class Teacher extends Person {

    private long id;
    private List<Course> courses;

    public Teacher() {
        super();
    }

    public long getId() {
        return id;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Teacher [toString()="
                + super.toString() + ", courses=" + courses + "]";
    }

}
