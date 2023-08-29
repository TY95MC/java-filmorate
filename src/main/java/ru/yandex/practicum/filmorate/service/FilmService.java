package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    private final FilmStorage filmStorage;

    private final Comparator<Film> comparator = new Comparator<>() {
        @Override
        public int compare(Film f1, Film f2) {
            if (f1.getLikes().size() == f2.getLikes().size()) {
                return 0;
            }
            return (f1.getLikes().size() > f2.getLikes().size()) ? -1 : 1;
        }
    };

    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        if (!filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            filmStorage.getFilmById(filmId).getLikes().add(userId);
        } else {
            throw new ValidationException("Пользователь уже лайкнул этот фильм.");
        }
    }

    public void deleteLike(int filmId, Integer userId) {
        if (filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            filmStorage.getFilmById(filmId).getLikes().remove(userId);
        } else {
            throw new EntityNotFoundException("Пользователя нет в списках лайкнувших.");
        }
    }

    public List<Film> getPopularFilms(int limit) {
        return filmStorage.getFilms().stream()
                .sorted(comparator)
                .limit(limit)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(@Valid Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(@Valid Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

}
