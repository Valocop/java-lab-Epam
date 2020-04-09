package com.epam.lab.repository;

import com.epam.lab.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User save(User entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public User update(User entity) {
        return entityManager.merge(entity);
    }

    @Override
    public Optional<User> findById(Long aLong) {
        return findByCriteria("id", aLong);
    }

    @Override
    public void delete(User entity) {
        User user = entityManager.contains(entity) ? entity : entityManager.merge(entity);
        entityManager.remove(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findByCriteria("username", username);
    }

    private Optional<User> findByCriteria(String column, Object value) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Predicate predicate = criteriaBuilder.equal(root.get(column), value);
        criteriaQuery.select(root).where(predicate);
        return entityManager.createQuery(criteriaQuery).getResultList().stream()
                .findFirst();
    }
}
