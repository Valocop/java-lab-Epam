package com.epam.lab.repository;

import com.epam.lab.model.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepoImpl implements AuthorRepo {
    private static final String INSERT = "insert into news.author (name, surname) values (?, ?)";
    private static final String SELECT_ALL = "select id, name, surname from news.author";
    private static final String UPDATE = "update news.author set name = ?, surname = ? where id = ?";
    private static final String DELETE = "delete from news.author where id = ?";
    private static final RowMapper<Author> authorMapper = (resultSet, i) -> {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String surname = resultSet.getString("surname");
        return new Author(id, name, surname);
    };
    private JdbcTemplate jdbcTemplate;

    public AuthorRepoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long save(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, author.getName());
            preparedStatement.setString(2, author.getSurname());
            return preparedStatement;
        }, keyHolder);
        return (Long) keyHolder.getKey();
    }

    @Override
    public boolean update(Author author) {
        return jdbcTemplate.update(UPDATE, author.getName(), author.getSurname(), author.getId()) == 1;
    }

    @Override
    public boolean delete(Author author) {
        return jdbcTemplate.update(DELETE, author.getId()) == 1;
    }

    @Override
    public List<Author> find(QuerySpecification spec) {
        List<Author> authorList = new ArrayList<>();

        if (spec == null) {
            return authorList;
        } else {
            return jdbcTemplate.query(spec.query(), authorMapper);
        }
    }

    @Override
    public List<Author> findAll() {
        return jdbcTemplate.query(SELECT_ALL, authorMapper);
    }
}
