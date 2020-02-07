package com.epam.lab.repository;

import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.specification.QuerySpecification;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NewsRepoImpl implements NewsRepo {
    private static final String INSERT = "insert into news (title, short_text, full_text, creation_date, " +
            "modification_date) values (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "select id, title, short_text, full_text, creation_date, " +
            "modification_date from news";
    private static final String UPDATE = "update news set title = ?, short_text = ?, full_text = ?, " +
            "creation_date = ?, modification_date = ?";
    private static final String DELETE = "delete from news where id = ?";
    private static final String INSERT_NEWS_AUTHOR = "insert into news_author (news_id, author_id) values (?, ?)";
    private static final String DELETE_AUTHOR_OF_NEWS = "delete from news_author where news_id = ?";
    private static final String DELETE_NEWS_OF_AUTHOR = "delete from news_author where author_id = ?";
    private static final String INSERT_TAG_TO_NEWS = "insert into news_tag (news_id, tag_id) values(?, ?)";
    private static final String DELETE_TAG_OF_NEWS = "delete from news_tag where news_id = ? and tag_id = ?";
    private static final String DELETE_TAGS_OF_NEWS = "delete from news_tag where news_id = ?";
    private static final RowMapper<News> newsMapper = (rs, rowNum) -> {
        long id = rs.getLong("id");
        String title = rs.getString("title");
        String shortText = rs.getString("short_text");
        String fullText = rs.getString("full_text");
        Date creationDate = rs.getDate("creation_date");
        Date modificationDate = rs.getDate("modification_date");
        return new News(id, title, shortText, fullText, creationDate, modificationDate);
    };
    private JdbcTemplate jdbcTemplate;

    public NewsRepoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long save(News news) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, new String[]{"id"});
            int i = 0;
            preparedStatement.setString(++i, news.getTitle());
            preparedStatement.setString(++i, news.getShortText());
            preparedStatement.setString(++i, news.getFullText());
            preparedStatement.setDate(++i, news.getCreationDate());
            preparedStatement.setDate(++i, news.getModificationDate());
            return preparedStatement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public boolean update(News news) {
        return jdbcTemplate.update(UPDATE, news.getTitle(), news.getShortText(), news.getFullText(),
                news.getCreationDate(), news.getModificationDate()) == 1;
    }

    @Override
    public boolean delete(News news) {
        return jdbcTemplate.update(DELETE, news.getId()) == 1;
    }

    @Override
    public List<News> find(QuerySpecification spec) {
        List<News> newsList = new ArrayList<>();

        if (spec == null) {
            return newsList;
        }
        try {
            return jdbcTemplate.query(spec.query(), newsMapper);
        } catch (DataAccessException e) {
            return newsList;
        }
    }

    @Override
    public List<News> findAll() {
        return jdbcTemplate.query(SELECT_ALL, newsMapper);
    }

    @Override
    public boolean createAuthorToNews(News news, Author author) {
        try {
            return jdbcTemplate.update(INSERT_NEWS_AUTHOR, news.getId(), author.getId()) == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean deleteAuthorOfNews(News news) {
        try {
            return jdbcTemplate.update(DELETE_AUTHOR_OF_NEWS, news.getId()) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean deleteNewsOfAuthor(Author author) {
        try {
            return jdbcTemplate.update(DELETE_NEWS_OF_AUTHOR, author.getId()) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean createTagToNews(News news, Tag tag) {
        try {
            return jdbcTemplate.update(INSERT_TAG_TO_NEWS, news.getId(), tag.getId()) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean deleteTagOfNews(News news, Tag tag) {
        try {
            return jdbcTemplate.update(DELETE_TAG_OF_NEWS, news.getId(), tag.getId()) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean deleteTagsOfNews(News news) {
        try {
            return jdbcTemplate.update(DELETE_TAGS_OF_NEWS, news.getId()) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }
}
