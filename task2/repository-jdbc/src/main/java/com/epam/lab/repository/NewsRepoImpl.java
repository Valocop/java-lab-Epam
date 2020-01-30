package com.epam.lab.repository;

import com.epam.lab.model.News;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class NewsRepoImpl implements NewsRepo {
    private static final String INSERT = "insert into news.news (title, short_text, full_text, creation_date, " +
            "modification_date) values (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL = "select id, title, short_text, full_text, creation_date, " +
            "modification_date from news.news";
    private static final String UPDATE = "update news.news set title = ?, short_text = ?, full_text = ?, " +
            "creation_date = ?, modification_date = ?";
    private static final String DELETE = "delete from news.news where id = ?";
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
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            int i = 0;
            preparedStatement.setString(++ i, news.getTitle());
            preparedStatement.setString(++ i, news.getShortText());
            preparedStatement.setString(++ i, news.getFullText());
            preparedStatement.setDate(++ i, news.getCreationDate());
            preparedStatement.setDate(++ i, news.getModificationDate());
            return preparedStatement;
        }, keyHolder);
        return (Long) keyHolder.getKey();
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
        } else {
            return jdbcTemplate.query(spec.query(), newsMapper);
        }
    }

    @Override
    public List<News> findAll() {
        return jdbcTemplate.query(SELECT_ALL, newsMapper);
    }
}
