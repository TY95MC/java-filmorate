package ru.yandex.practicum.filmorate.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("mpaRatingDBStorage")
public class MpaRatingDBStorage implements MpaRatingStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MpaRating> findAllMpaRatings() {
        String sql = "SELECT * FROM rating";
        return jdbcTemplate.query(sql, new MpaRatingMapper());
    }

    @Override
    public MpaRating findMpaRatingById(int id) {
        return checkIfExists(id);
    }

    private MpaRating checkIfExists(int id) {
        try {
            String sql = "SELECT * FROM rating WHERE rating_id = ?";
            return jdbcTemplate.queryForObject(sql, new MpaRatingMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Рейтинга с id:" + id + " не существует.");
        }
    }

    static class MpaRatingMapper implements RowMapper<MpaRating> {
        @Override
        public MpaRating mapRow(ResultSet rs, int rowNum) throws SQLException {
            return MpaRating.builder()
                    .id(rs.getInt("rating_id"))
                    .name(rs.getString("rating_name"))
                    .build();
        }
    }
}
