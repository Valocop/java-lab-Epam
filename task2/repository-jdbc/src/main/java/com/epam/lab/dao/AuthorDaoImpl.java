package com.epam.lab.dao;

import com.epam.lab.model.Author;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class AuthorDaoImpl implements AuthorDao {
    private static final String INSERT = "insert into news.author (name, surname) values (?, ?)";
    private static final String SELECT_ALL = "select id, name, surname from news.author";
    private static final String SELECT_BY_ID = "select id, name, surname from news.author where id = ?";
    private static final String UPDATE = "update news.author set name = ?, surname = ? where id = ?";
    private static final String DELETE = "delete from news.author where id = ?";
    private static final RowMapper<Author> authorMapper = (resultSet, i) -> {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String surname = resultSet.getString("surname");
        return new Author(id, name, surname);
    };
    private JdbcTemplate jdbcTemplate;

    public AuthorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, author.getId());
            return preparedStatement;
        }, keyHolder);
        int result = (int) keyHolder.getKey();
        return result > 0;
    }

    @Override
    public boolean delete(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, author.getId());
            return preparedStatement;
        }, keyHolder);
        int result = (int) keyHolder.getKey();
        return result > 0;
    }

    @Override
    public Author getById(Long id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID, authorMapper, id);
    }

    @Override
    public List<Author> findAll() {
        return jdbcTemplate.query(SELECT_ALL, authorMapper);
    }
}
