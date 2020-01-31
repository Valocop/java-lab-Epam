package com.epam.lab.repository;

import com.epam.lab.model.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TagRepoImpl implements TagRepo {
    private static final String INSERT = "insert into news.tag (name) values (?)";
    private static final String SELECT_ALL = "select id, name from news.tag";
    private static final String UPDATE = "update news.tag set name = ?";
    private static final String DELETE = "delete from news.tag where id = ?";
    private static final RowMapper<Tag> tagMapper = (resultSet, i) -> {
        long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        return new Tag(id, name);
    };
    private JdbcTemplate jdbcTemplate;

    public TagRepoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long save(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tag.getName());
            return preparedStatement;
        }, keyHolder);
        return (Long) keyHolder.getKey();
    }

    @Override
    public boolean update(Tag tag) {
        return jdbcTemplate.update(UPDATE, tag.getName()) == 1;
    }

    @Override
    public boolean delete(Tag tag) {
        return jdbcTemplate.update(DELETE, tag.getId()) == 1;
    }

    @Override
    public List<Tag> find(QuerySpecification spec) {
        List<Tag> tagList = new ArrayList<>();

        if (spec == null) {
            return tagList;
        } else {
            return jdbcTemplate.query(spec.query(), tagMapper);
        }
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(SELECT_ALL, tagMapper);
    }
}
