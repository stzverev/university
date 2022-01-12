package ua.com.foxminded.university.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
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

}
