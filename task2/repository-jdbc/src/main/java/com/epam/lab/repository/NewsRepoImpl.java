package com.epam.lab.repository;

import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;
import com.epam.lab.specification.FindSpecification;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewsRepoImpl implements NewsRepo {
    private static final String INSERT = "insert into news (title, short_text, full_text, creation_date, " +
            "modification_date) values (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "select id, title, short_text, full_text, creation_date, " +
            "modification_date from news";
    private static final String UPDATE = "update news set title = ?, short_text = ?, full_text = ?, " +
            "creation_date = ?, modification_date = ? where id = ?";
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
        LocalDate creationDate = rs.getDate("creation_date").toLocalDate();
        LocalDate modificationDate = rs.getDate("modification_date").toLocalDate();
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
            preparedStatement.setDate(++i, Date.valueOf(news.getCreationDate()));
            preparedStatement.setDate(++i, Date.valueOf(news.getModificationDate()));
            return preparedStatement;
        }, keyHolder);
        return (Long) keyHolder.getKey();
    }

    @Override
    public boolean update(News news) {
        try {
            return jdbcTemplate.update(UPDATE, news.getTitle(), news.getShortText(), news.getFullText(),
                    Date.valueOf(news.getCreationDate()), Date.valueOf(news.getModificationDate()), news.getId()) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean delete(News news) {
        return jdbcTemplate.update(DELETE, news.getId()) > 0;
    }

    @Override
    public List<News> findBy(FindSpecification spec) {
        List<News> newsList = new ArrayList<>();
        try {
            return spec == null ? newsList : jdbcTemplate.query(spec.query(), newsMapper);
        } catch (DataAccessException e) {
            return newsList;
        }
    }

    @Override
    public List<News> findAll() {
        return jdbcTemplate.query(SELECT_ALL, newsMapper);
    }

    @Override
    public boolean createBindingOfAuthorAndNews(News news, Author author) {
        try {
            return jdbcTemplate.update(INSERT_NEWS_AUTHOR, news.getId(), author.getId()) == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean deleteBindingsOfNewsAndAutors(News news) {
        try {
            return jdbcTemplate.update(DELETE_AUTHOR_OF_NEWS, news.getId()) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean deleteBindingsOfAuthorAndNews(Author author) {
        try {
            return jdbcTemplate.update(DELETE_NEWS_OF_AUTHOR, author.getId()) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean createBindingOfNewsAndTags(News news, Tag tag) {
        try {
            return jdbcTemplate.update(INSERT_TAG_TO_NEWS, news.getId(), tag.getId()) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean deleteBindingOfNewsAndTag(News news, Tag tag) {
        try {
            return jdbcTemplate.update(DELETE_TAG_OF_NEWS, news.getId(), tag.getId()) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean deleteBindingsOfNewsAndAllTags(News news) {
        try {
            return jdbcTemplate.update(DELETE_TAGS_OF_NEWS, news.getId()) > 0;
        } catch (DataAccessException e) {
            return false;
        }
    }
}
