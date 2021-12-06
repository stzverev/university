package ua.com.foxminded.university.exceptions;

import java.io.Serializable;

public class ObjectNotFoundById extends RuntimeException {

    private static final long serialVersionUID = -670934109651074785L;

    public ObjectNotFoundById(long id, Class<? extends Serializable> entityClass) {
        super(String.format("%s not found by id: %s", entityClass.getName(), id));
    }

}
