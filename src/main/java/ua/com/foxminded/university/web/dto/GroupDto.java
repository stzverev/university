package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.util.List;

public class GroupDto implements Serializable {

    private static final long serialVersionUID = 2395786904105415866L;

    private Long id;
    private String name;
    private List<String> courses;

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

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public GroupDto() {
        super();
    }


}
