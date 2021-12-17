package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.util.List;

public class TeacherDto implements Serializable {

    private static final long serialVersionUID = 4444782064687733846L;

    private Long id;
    private String firstName;
    private String lastName;
    private List<String> courses;

    public TeacherDto() {
        super();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

}
