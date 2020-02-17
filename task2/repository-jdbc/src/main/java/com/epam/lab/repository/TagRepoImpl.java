package com.epam.lab.repository;

import com.epam.lab.model.Tag;
import com.epam.lab.specification.FindSpecification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class TagRepoImpl implements TagRepo {
    private static final String INSERT = "insert into tag (name) values (?)";
    private static final String SELECT_ALL = "select id, name from tag";
    private static final String UPDATE = "update tag set name = ? where id = ?";
    private static final String DELETE = "delete from tag where id = ?";
    private static final String DELETE_UNSIGNED_TAGS = "delete from tag where tag.id in " +
            "(select tag.id from tag left join news_tag on news_tag.tag_id = tag.id " +
            "where news_tag.news_id IS NULL and news_tag.tag_id IS NULL)";
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
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT, new String[]{"id"});
            preparedStatement.setString(1, tag.getName());
            return preparedStatement;
        }, keyHolder);
        return (Long) keyHolder.getKey();
    }

    @Override
    public boolean update(Tag tag) {
        return jdbcTemplate.update(UPDATE, tag.getName(), tag.getId()) == 1;
    }

    @Override
    public boolean delete(Tag tag) {
        return jdbcTemplate.update(DELETE, tag.getId()) == 1;
    }

    @Override
    public List<Tag> findBy(FindSpecification spec) {
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

    @Override
    public boolean deleteUnsignedTags() {
        return jdbcTemplate.update(DELETE_UNSIGNED_TAGS) > 0;
    }
}
