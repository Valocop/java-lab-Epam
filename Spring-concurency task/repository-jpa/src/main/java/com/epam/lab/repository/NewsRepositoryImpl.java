package com.epam.lab.repository;

import com.epam.lab.model.News;
import com.epam.lab.specification.NewsSearchSpecification;
import com.epam.lab.specification.SearchCriteria;
import com.epam.lab.specification.SearchSpecification;
import com.epam.lab.specification.SortSpecification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.lab.specification.NewsSearchSpecification.NEWS_ID;

@Repository("newsRepository")
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
    public List<News> findAll(SearchSpecification<News> searchSpec) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Root<News> newsRoot = criteriaQuery.from(News.class);
        Predicate predicate = searchSpec.toPredicate(newsRoot, criteriaQuery, criteriaBuilder);
        criteriaQuery.select(newsRoot).where(predicate);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<News> findAll(SortSpecification<News> sortSpec, Integer count, Integer page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Root<News> newsRoot = criteriaQuery.from(News.class);
        Order order = sortSpec.toOrder(newsRoot, criteriaQuery, criteriaBuilder);
        CriteriaQuery<News> select = criteriaQuery.select(newsRoot).orderBy(order);

        TypedQuery<News> typedQuery = entityManager.createQuery(select);
        return getNews(count, page, typedQuery, count());
    }

    @Override
    public List<News> findAll(SearchSpecification<News> searchSpec, SortSpecification<News> sortSpec,
                              Integer count, Integer page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> criteriaQuery = criteriaBuilder.createQuery(News.class);
        Root<News> newsRoot = criteriaQuery.from(News.class);
        Order order = sortSpec.toOrder(newsRoot, criteriaQuery, criteriaBuilder);
        Predicate predicate = searchSpec.toPredicate(newsRoot, criteriaQuery, criteriaBuilder);
        CriteriaQuery<News> select = criteriaQuery.select(newsRoot).where(predicate).orderBy(order);

        TypedQuery<News> typedQuery = entityManager.createQuery(select);
        return getNews(count, page, typedQuery, count(searchSpec));
    }

    private List<News> getNews(Integer count, Integer page, TypedQuery<News> typedQuery, long totalCount) {
        if (page <= totalCount) {
            typedQuery.setFirstResult((page * count) - count);
            typedQuery.setMaxResults(count);
            return typedQuery.getResultList();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public long count(SearchSpecification<News> searchSpec) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<News> newsRoot = criteriaQuery.from(News.class);

        criteriaQuery.select(criteriaBuilder.count(newsRoot));
        Predicate predicate = searchSpec.toPredicate(newsRoot, criteriaQuery, criteriaBuilder);
        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public void delete(News entity) {
        News news = entityManager.contains(entity) ? entity : entityManager.merge(entity);
        entityManager.remove(news);
    }

    @Override
    public long count() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(News.class)));
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
