package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Component("filmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {

    final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);


    public FilmDbStorage(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films";
        log.info("Сработал FilmDbStorage");
        return jdbcTemplate.query(sql, new FilmMapper());
    }

    @Override
    public Film addFilm(Film film) {
        log.info("Сработал FilmDbStorage");
        String sql = "INSERT INTO films(name, description, release_date, duration) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder id = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setString(3, film.getReleaseDate().toString());
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, id);
        film.setId(Objects.requireNonNull(id.getKey()).intValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Film getFilmById(int id) {
        return null;
    }

    @Override
    public void addLike(int filmId, int userId) {
    }

    @Override
    public void deleteLike(int filmId, Integer userId) {

    }

    @Override
    public List<Film> getPopularFilms(int limit) {
        return null;
    }

    static class FilmMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Film
                    .builder()
                    .id(rs.getInt("film_id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date"))
                    .duration(rs.getInt("duration"))
                    .likes(new HashSet<Integer>(rs.getInt("user_id")))
                    .genres(new HashSet<Integer>(rs.getInt("genre_id")))
                    .mpa(new MpaRating(rs.getInt("rating_id"), rs.getString("rating_name")))
                    .build();
        }
    }
}
