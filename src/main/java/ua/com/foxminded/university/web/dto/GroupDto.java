package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

@Data
public class GroupDto implements Serializable {

    private static final long serialVersionUID = 2395786904105415866L;
    private static final int NAME_MAX_LENGTH = 150;

    private Long id;

    @NotBlank
    @Size(max = NAME_MAX_LENGTH)
    private String name;

    @Hidden
    private List<CourseDto> courses;

}
