package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Data
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

    @Hidden
    private List<CourseDto> courses;

}
