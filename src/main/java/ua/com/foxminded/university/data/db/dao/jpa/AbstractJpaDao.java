package ua.com.foxminded.university.data.db.dao.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import ua.com.foxminded.university.data.db.dao.GenericDao;

@Transactional
public class AbstractJpaDao<T extends Serializable> implements GenericDao<T> {

    private final Class<T> clazz;

    @PersistenceContext
    private EntityManager entityManager;

    public AbstractJpaDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void save(List<T> list) {
        list.stream().forEach(entityManager::persist);
    }

    @Override
    public void save(T object) {
        entityManager.persist(object);
    }

    @Override
    public void update(T object) {
        entityManager.merge(object);
    }

    @Override
    public void delete(long id) {
        T object = entityManager.find(clazz, id);
        entityManager.remove(
                (entityManager.contains(object)) ? object : entityManager.merge(object));
    }

    @Override
    public Optional<T> getById(long id) {
        Optional<T> optional = Optional.ofNullable(entityManager.find(clazz, id));

        if (optional.isPresent()) {
            entityManager.detach(optional.get());
        }
        return optional;
    }

    @Override
    public List<T> getAll() {
        List<T> objects = entityManager.createQuery("FROM " + clazz.getName(), clazz)
                .getResultList();
        objects.stream().forEach(entityManager::detach);
        return objects;
    }

}
