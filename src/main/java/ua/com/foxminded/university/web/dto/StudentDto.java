package ua.com.foxminded.university.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class StudentDto implements Serializable {

    private static final long serialVersionUID = 3476497836448273698L;
    private static final int FIRST_NAME_MAX_LENGTH = 100;
    private static final int LAST_NAME_MAX_LENGTH = 100;

    private Long id;

    @NotBlank
    @Size(max = FIRST_NAME_MAX_LENGTH)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(max = LAST_NAME_MAX_LENGTH)
    private String lastName;

    private Long groupId;
    private String groupName;

    public StudentDto() {
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
