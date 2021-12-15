package ua.com.foxminded.university.web.mapper;

import java.io.Serializable;

public interface GenericMapper<E extends Serializable, D extends Serializable> {

    D toDto(E entity);

}
