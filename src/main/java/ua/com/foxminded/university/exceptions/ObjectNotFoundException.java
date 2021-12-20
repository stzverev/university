package ua.com.foxminded.university.exceptions;

import java.io.Serializable;

public class ObjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -670934109651074785L;

    public ObjectNotFoundException(long id, Class<? extends Serializable> entityClass) {
        super(String.format("%s not found by id: %s", entityClass.getName(), id));
    }
    
    public ObjectNotFoundException(Class<? extends Serializable> entityClass) {
        super(String.format("%s not found", entityClass.getName()));
    }

}
