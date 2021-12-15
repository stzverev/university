package ua.com.foxminded.university.web.mapper.impl;

import java.io.Serializable;

import org.modelmapper.ModelMapper;

import ua.com.foxminded.university.web.mapper.GenericMapper;

public abstract class GenericMapperAbstract<E extends Serializable, D extends Serializable>
        implements GenericMapper<E, D> {

    protected final Class<E> entityClass;
    protected final Class<D> dtoClass;
    private final ModelMapper modelMapper = new ModelMapper();

    protected GenericMapperAbstract(Class<E> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    protected ModelMapper getModelMapper() {
        return modelMapper;
    }

    @Override
    public D toDto(E entity) {
        return modelMapper.map(entity, dtoClass);
    }

}
