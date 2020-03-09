package com.epam.lab.repository;

import com.epam.lab.model.Author;
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
public class AuthorRepositoryImpl implements AuthorRepository {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Author save(Author entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Author update(Author entity) {
        return entityManager.merge(entity);
    }

    @Override
    public Optional<Author> findById(Long aLong) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> criteriaQuery = criteriaBuilder.createQuery(Author.class);
        Root<Author> root = criteriaQuery.from(Author.class);
        Predicate predicate = criteriaBuilder.equal(root.get("id"), aLong);
        criteriaQuery.select(root).where(predicate);
        return entityManager.createQuery(criteriaQuery).getResultList().stream()
                .findFirst();
    }

    @Override
    public void delete(Author entity) {
        Author author = entityManager.contains(entity) ? entity : entityManager.merge(entity);
        entityManager.remove(author);
    }
}
