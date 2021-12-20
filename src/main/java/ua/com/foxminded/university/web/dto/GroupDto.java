package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.util.List;

public class GroupDto implements Serializable {

    private static final long serialVersionUID = 2395786904105415866L;

    private Long id;
    private String name;
    private List<CourseDto> courses;

    public GroupDto() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseDto> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseDto> courses) {
        this.courses = courses;
    }

}
