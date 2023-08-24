package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    private final InMemoryFilmStorage filmStorage;

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
        if (filmId < 1 && userId < 1) {
            throw new ValidationException("Введен неверный идентификатор!");
        } else if (filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            filmStorage.getFilmById(filmId).getLikes().remove(userId);
        } else {
            throw new UserNotFoundException("Пользователя нет в списках лайкнувших.");
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

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

}
