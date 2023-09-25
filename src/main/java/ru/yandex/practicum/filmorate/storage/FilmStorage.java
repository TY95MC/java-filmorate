package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(int id);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, Integer userId);

    List<Film> getPopularFilms(int limit);

//    List<Genre> findAllGenres();
//
//    Genre findGenreById(int id);
}
