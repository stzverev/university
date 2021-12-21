package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CourseDto implements Serializable {

    private static final long serialVersionUID = 362044286549937480L;
    private static final int NAME_MAX_LENGTH = 150;

    private Long id;

    @NotBlank
    @Size(max = NAME_MAX_LENGTH)
    private String name;

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

}
