package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TeacherDto implements Serializable {

    private static final long serialVersionUID = 4444782064687733846L;
    private static final int FIRST_NAME_MAX_LENGTH = 100;
    private static final int LAST_NAME_MAX_LENGTH = 100;

    private Long id;

    @NotBlank
    @Size(max = FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @NotBlank
    @Size(max = LAST_NAME_MAX_LENGTH)
    private String lastName;

    private List<CourseDto> courses;

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

    public List<CourseDto> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseDto> courses) {
        this.courses = courses;
    }

}
