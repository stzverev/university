package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.Hidden;

public class GroupDto implements Serializable {

    private static final long serialVersionUID = 2395786904105415866L;
    private static final int NAME_MAX_LENGTH = 150;

    private Long id;

    @NotBlank
    @Size(max = NAME_MAX_LENGTH)
    private String name;

    @Hidden
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

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GroupDto other = (GroupDto) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }

}
