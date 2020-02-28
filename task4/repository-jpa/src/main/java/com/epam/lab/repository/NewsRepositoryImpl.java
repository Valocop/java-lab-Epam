package com.epam.lab.repository;

import com.epam.lab.model.News;
import com.epam.lab.specification.NewsSearchSpecification;
import com.epam.lab.specification.SearchCriteria;
import com.epam.lab.specification.SearchSpecification;
import com.epam.lab.specification.SortSpecification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

import static com.epam.lab.specification.NewsSearchSpecification.NEWS_ID;

@Repository
@Transactional
public class NewsRepositoryImpl implements NewsRepository {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public News save(News entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public News update(News entity) {
        return entityManager.merge(entity);
    }

    @Override
    public Optional<News> findById(Long aLong) {
        NewsSearchSpecification searchSpec = new NewsSearchSpecification(new SearchCriteria(NEWS_ID, aLong.toString()));
        return findAll(searchSpec).stream()
                .findFirst();
    }

    @Override
    public List<News> findAll(SearchSpecification<News> searchSpec, SortSpecification<News> sortSpec) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Root<News> newsRoot = criteriaQuery.from(News.class);
        Predicate predicate = searchSpec.toPredicate(newsRoot, criteriaQuery, criteriaBuilder);
        Order order = sortSpec.toOrder(newsRoot, criteriaQuery, criteriaBuilder);
        criteriaQuery.select(newsRoot).where(predicate).orderBy(order);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<News> findAll(SearchSpecification<News> searchSpec) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Root<News> newsRoot = criteriaQuery.from(News.class);
        Predicate predicate = searchSpec.toPredicate(newsRoot, criteriaQuery, criteriaBuilder);
        criteriaQuery.select(newsRoot).where(predicate);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public void delete(News entity) {
        entityManager.remove(entity);
    }

    @Override
    public long count() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(News.class)));
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
