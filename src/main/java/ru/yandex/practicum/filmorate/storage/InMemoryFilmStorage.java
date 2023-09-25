package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final Map<Integer, Film> idToFilm = new HashMap<>();
    private int id = 1;
    private final Comparator<Film> comparator = new Comparator<>() {
        @Override
        public int compare(Film f1, Film f2) {
            if (f1.getLikes().size() == f2.getLikes().size()) {
                return 0;
            }
            return (f1.getLikes().size() > f2.getLikes().size()) ? -1 : 1;
        }
    };

    @Override
    public List<Film> getFilms() {
        log.info("Количество фильмов в текущий момент: {}", idToFilm.size());
        return List.copyOf(idToFilm.values());
    }

    @Override
    public Film addFilm(Film film) {
        if (!idToFilm.containsKey(film.getId())) {
            film.setId(generateId());
            film.setLikes(new HashSet<>());
            idToFilm.put(film.getId(), film);
            log.info("Сохраняется фильм {}.", film.toString());
            return film;
        } else {
            log.info("Попытка зарегистрировать уже существующий фильм {}.", film.getId());
            throw new ValidationException("Ошибка! Такой фильм уже существует!");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        if (!idToFilm.containsKey(film.getId())) {
            log.info("Попытка обновить несуществующий фильм {}.", film.toString());
            throw new EntityNotFoundException("Ошибка! Фильма с таким идентификатором нет!");
        } else {
            if (film.getLikes() == null) {
                film.setLikes(idToFilm.get(film.getId()).getLikes());
            }
            idToFilm.put(film.getId(), film);
            log.info("Обновление данных фильма {}.", film.toString());
            return film;
        }
    }

    @Override
    public Film getFilmById(int id) {
        if (!idToFilm.containsKey(id)) {
            log.info("Попытка извлечь несуществующий фильм {}.", id);
            throw new EntityNotFoundException("Ошибка! Фильма с таким идентификатором нет!");
        } else {
            return idToFilm.get(id);
        }
    }

    public void addLike(int filmId, int userId) {
        if (!idToFilm.get(filmId).getLikes().contains(userId)) {
            idToFilm.get(filmId).getLikes().add(userId);
        } else {
            throw new ValidationException("Пользователь уже лайкнул этот фильм.");
        }
    }

    public void deleteLike(int filmId, Integer userId) {
        if (idToFilm.get(filmId).getLikes().contains(userId)) {
            idToFilm.get(filmId).getLikes().remove(userId);
        } else {
            throw new EntityNotFoundException("Пользователя нет в списках лайкнувших.");
        }
    }

    public List<Film> getPopularFilms(int limit) {
        return this.getFilms().stream()
                .sorted(comparator)
                .limit(limit)
                .collect(Collectors.toUnmodifiableList());
    }

    private int generateId() {
        return id++;
    }
}
