package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TabletimeDto implements Serializable {

    private static final long serialVersionUID = -6001766765168537465L;

    private Long id;

    private LocalDateTime dateTime;
    private String groupName;
    private String courseName;
    private String teacherFirstName;
    private String teacherLastName;

}
