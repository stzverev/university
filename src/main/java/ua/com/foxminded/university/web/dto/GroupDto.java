package ua.com.foxminded.university.web.dto;

import java.io.Serializable;

public class GroupDto implements Serializable {

    private static final long serialVersionUID = 2395786904105415866L;

    private Long id;
    private String name;

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

    public GroupDto() {
        super();
    }

}
