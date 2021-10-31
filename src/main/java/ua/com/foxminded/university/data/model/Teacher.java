package ua.com.foxminded.university.data.model;

import java.util.List;

public class Teacher extends Person {

    private List<Course> courses;

    public Teacher() {
        super();
    }

    public List<Course> getCourses() {
        return courses;
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
