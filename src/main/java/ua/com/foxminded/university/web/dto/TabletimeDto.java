package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NonNull;

@Data
public class TabletimeDto implements Serializable {

    private static final long serialVersionUID = -6001766765168537465L;

    private Long id;

    @NonNull
    private LocalDateTime dateTime;

    @NonNull
    private String groupName;

    @NonNull
    private String courseName;

    @NonNull
    private String teacherFirstName;

    @NonNull
    private String teacherLastName;

}
