package ua.com.foxminded.university.web.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NonNull;

@Data
public class TabletimeDto implements Serializable {

    private static final long serialVersionUID = -6001766765168537465L;

    private Long id;

    private @NonNull LocalDateTime dateTime;
    private @NonNull String groupName;
    private @NonNull String courseName;
    private @NonNull String teacherFirstName;
    private @NonNull String teacherLastName;

}
