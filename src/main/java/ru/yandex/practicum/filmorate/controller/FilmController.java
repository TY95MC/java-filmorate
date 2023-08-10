package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static int id = 1;

    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Количество фильмов в текущий момент: {}", films.size());
        return Collections.unmodifiableCollection(films.values());
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            log.info("Попытка зарегистрировать уже существующий фильм {}", film.toString());
            throw new FilmValidationException("Фильм " + film.getName() + " уже зарегистрирован!");
        }

        if (!isValidFilm(film)) {
            log.info("Попытка зарегистрировать некорректный фильм {}", film.toString());
            throw new FilmValidationException("Данные фильма некорректно заполнены!");
        } else {
            film.setId(generateId());
            log.info("Сохраняется фильм {}", film.toString());
            films.put(film.getId(), film);
            return film;
        }
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Попытка обновить не существующий фильм {}", film.toString());
            throw new FilmValidationException("Данные фильма некорректно заполнены!");
        }

        if (!isValidFilm(film)) {
            log.info("Попытка обновить некорректный фильм {}", film.toString());
            throw new FilmValidationException("Данные фильма некорректно заполнены!");
        }

        log.info("Обновление данных фильма {}", film.toString());
        films.put(film.getId(), film);
        return film;

    }

    private boolean isValidFilm(Film film) {
        return !film.getName().isBlank()
                && film.getDescription().length() < 200
                && film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))
                && film.getDuration() > 0;
    }

    private int generateId() {
        return id++;
    }
}
