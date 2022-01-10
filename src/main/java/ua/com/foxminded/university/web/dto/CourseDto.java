package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class CourseDto implements Serializable {

    private static final long serialVersionUID = 362044286549937480L;
    private static final int NAME_MAX_LENGTH = 150;

    private Long id;

    @NotBlank
    @Size(max = NAME_MAX_LENGTH)
    private String name;

    @Hidden
    private List<GroupDto> groups;

    public CourseDto() {
        super();
    }

    public List<GroupDto> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDto> groups) {
        this.groups = groups;
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
        CourseDto other = (CourseDto) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }

}
