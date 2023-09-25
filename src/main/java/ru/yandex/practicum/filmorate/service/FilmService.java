package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
//    private final GenreStorage genreStorage;
//    private final Comparator<Film> comparator = new Comparator<>() {
//        @Override
//        public int compare(Film f1, Film f2) {
//            if (f1.getLikes().size() == f2.getLikes().size()) {
//                return 0;
//            }
//            return (f1.getLikes().size() > f2.getLikes().size()) ? -1 : 1;
//        }
//    };
private static final Logger log = LoggerFactory.getLogger(FilmService.class);


    public FilmService(final FilmStorage filmStorage/*, final GenreStorage genreStorage*/) {
        this.filmStorage = filmStorage;
//        this.genreStorage = genreStorage;
    }

    public void addLike(int filmId, int userId) {
//        if (!filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
//            filmStorage.getFilmById(filmId).getLikes().add(userId);
//        } else {
//            throw new ValidationException("Пользователь уже лайкнул этот фильм.");
//        }
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, Integer userId) {
//        if (filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
//            filmStorage.getFilmById(filmId).getLikes().remove(userId);
//        } else {
//            throw new EntityNotFoundException("Пользователя нет в списках лайкнувших.");
//        }
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int limit) {
//        return filmStorage.getFilms().stream()
//                .sorted(comparator)
//                .limit(limit)
//                .collect(Collectors.toUnmodifiableList());
        return filmStorage.getPopularFilms(limit);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        log.info("сработал FilmService");
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

//    public List<Genre> findAllGenres() {
//        return filmStorage.findAllGenres();
//    }
//
//    public Genre findGenreById(int id) {
//        return filmStorage.findGenreById(id);
//    }

}
