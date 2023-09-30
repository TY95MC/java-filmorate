package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component("filmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    private static final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);

    public FilmDbStorage(final JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films AS f " +
                "LEFT OUTER JOIN film_rating AS fr ON f.film_id = fr.film_id " +
                "LEFT OUTER JOIN rating AS r ON fr.rating_id = r.rating_id " +
                "ORDER BY f.film_id";
        return List.copyOf(jdbcTemplate.query(sql, new FilmMapper()));
    }

    @Override
    public Film addFilm(Film film) {
        String sqlFilms = "INSERT INTO films(film_name, film_description, " +
                "film_release_date, film_duration) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder id = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlFilms, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setString(3, film.getReleaseDate().toString());
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, id);
        film.setId(Objects.requireNonNull(id.getKey()).longValue());

        if (film.getMpa() != null) {
            jdbcTemplate.update("INSERT INTO film_rating VALUES (?, ?)", film.getId(), film.getMpa().getId());
        }

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO film_genre VALUES (?, ?)", film.getId(), genre.getId());
            }
        }

        if (film.getLikes() != null) {
            for (Long like : film.getLikes()) {
                jdbcTemplate.update("INSERT INTO film_likes VALUES (?, ?)", film.getId(), like);
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("обновление фильма с идентификатором {}", film.getId());
        if (checkIfFilmExists(film.getId())) {
            String sql = "UPDATE films " +
                    "SET film_name = ?, film_description = ?, film_release_date = ?, film_duration = ? " +
                    "WHERE film_id = ?";
            jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getId());

            if (film.getMpa() != null) {
                jdbcTemplate.update("UPDATE film_rating SET rating_id = ? WHERE film_id = ?",
                        film.getMpa().getId(), film.getId());
            }

            if (film.getGenres() != null) {
                jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = " + film.getId());
                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update("INSERT INTO film_genre VALUES(?, ?)", film.getId(), genre.getId());
                }
            }

            if (film.getLikes() != null) {
                jdbcTemplate.update("DELETE FROM film_likes WHERE film_id = " + film.getId());
                for (Long like : film.getLikes()) {
                    jdbcTemplate.update("INSERT INTO film_likes VALUES(?, ?)", film.getId(), like);
                }
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(Long id) {
        if (checkIfFilmExists(id)) {
            String sqlGetFilmById = "SELECT * FROM films AS f " +
                    "LEFT OUTER JOIN film_rating AS fr ON f.film_id = fr.film_id " +
                    "LEFT OUTER JOIN rating AS r ON fr.rating_id = r.rating_id " +
                    "WHERE f.film_id = " + id;
            return jdbcTemplate.queryForObject(sqlGetFilmById, new FilmMapper());
        }
        return null;
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        log.info("Лайк к фильму с id = {} от пользователя id = {}", filmId, userId);
        if (checkIfFilmExists(filmId)) {
            userDbStorage.checkIfUserExists(userId);
            jdbcTemplate.update("INSERT INTO film_likes VALUES (?, ?)", filmId, userId);
            return getFilmById(filmId);
        }
        return null;
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        log.info("Ударение лайка к фильму с id = {} от пользователя id = {}", filmId, userId);
        if (checkIfFilmExists(filmId)) {
            userDbStorage.checkIfUserExists(userId);
            jdbcTemplate.update("DELETE FROM film_likes WHERE film_id = ? AND user_id = ?", filmId, userId);
            return getFilmById(filmId);
        }
        return null;
    }

    @Override
    public List<Film> getPopularFilms(Long limit) {
        String sqlPopular = "SELECT f.film_id, f.film_name, f.film_description, f.film_release_date, " +
                "f.film_duration, fr.rating_id, r.rating_name " +
                "FROM films AS f " +
                "LEFT OUTER JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                "LEFT OUTER JOIN film_rating AS fr ON f.film_id = fr.film_id " +
                "LEFT OUTER JOIN rating AS r ON fr.rating_id = r.rating_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(fl.user_id) DESC " +
                "LIMIT " + limit;
        return List.copyOf(jdbcTemplate.query(sqlPopular, new FilmMapper()));
    }

    private boolean checkIfFilmExists(Long id) {
        try {
            if (id < 1) {
                log.info("Некорректный идентификатор id:" + id);
                throw new EntityNotFoundException("Некорректный идентификатор id:" + id);
            }
            String sql = "SELECT COUNT(film_id) FROM films WHERE film_id = " + id;
            if (jdbcTemplate.queryForObject(sql, Integer.class) == 0) {
                log.info("Несуществующий идентификатор id:" + id);
                throw new EntityNotFoundException("Фильм с id:" + id + " не найден.");
            }
            return true;
        } catch (EmptyResultDataAccessException e) {
            log.info("Некорректный идентификатор id:" + id);
            throw new EntityNotFoundException("Пользователя с id:" + id + " не существует.");
        }
    }

    private Set<Long> getIdSet(String sql) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        Set<Long> set = new HashSet<>();
        for (Map<String, Object> map : list) {
            for (Object tmp : map.values()) {
                String tmp1 = String.valueOf(tmp);
                set.add(Long.parseLong(tmp1));
            }
        }
        return set;
    }

    private class FilmMapper implements RowMapper<Film> {

        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Set<Long> genresId = getIdSet("SELECT genre_id FROM film_genre WHERE film_id = " +
                    rs.getLong("film_id"));
            Set<Genre> genreSet = new HashSet<>();
            for (Long id : genresId) {
                genreSet.add(jdbcTemplate.queryForObject("SELECT * FROM genre " +
                        "WHERE genre_id = " + id, new GenreDBStorage.GenreMapper()));
            }

            return Film
                    .builder()
                    .id(rs.getLong("film_id"))
                    .name(rs.getString("film_name"))
                    .description(rs.getString("film_description"))
                    .releaseDate(rs.getDate("film_release_date").toLocalDate())
                    .duration(rs.getInt("film_duration"))
                    .likes(getIdSet("SELECT user_id FROM film_likes WHERE film_id = " +
                            rs.getInt("film_id")))
                    .genres(genreSet)
                    .mpa(new MpaRating(rs.getInt("rating_id"), rs.getString("rating_name")))
                    .build();
        }
    }
}
