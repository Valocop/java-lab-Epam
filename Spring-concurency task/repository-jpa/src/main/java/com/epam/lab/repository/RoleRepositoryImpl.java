package com.epam.lab.repository;

import com.epam.lab.model.Role;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
@Transactional
public class RoleRepositoryImpl implements RoleRepository {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role save(Role entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Role update(Role entity) {
        return entityManager.merge(entity);
    }

    @Override
    public Optional<Role> findById(Long aLong) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
        Root<Role> root = criteriaQuery.from(Role.class);
        Predicate predicate = criteriaBuilder.equal(root.get("id"), aLong.toString());
        criteriaQuery.select(root).where(predicate);
        return entityManager.createQuery(criteriaQuery).getResultList().stream()
                .findFirst();
    }

    @Override
    public void delete(Role entity) {
        Role role = entityManager.contains(entity) ? entity : entityManager.merge(entity);
        entityManager.remove(role);
    }
}
