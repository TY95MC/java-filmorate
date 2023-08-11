package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static int id = 1;

    private final Map<Integer, Film> idToFilms = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public List<Film> getFilms() {
        log.info("Количество фильмов в текущий момент: {}", idToFilms.size());
        return List.copyOf(idToFilms.values());
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        if (idToFilms.containsKey(film.getId())) {
            log.info("Попытка зарегистрировать уже существующий фильм {}", film.toString());
            throw new ValidationException("Фильм " + film.getName() + " уже зарегистрирован!");
        }

        film.setId(generateId());
        log.info("Сохраняется фильм {}", film.toString());
        idToFilms.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!idToFilms.containsKey(film.getId())) {
            log.info("Попытка обновить не существующий фильм {}", film.toString());
            throw new ValidationException("Данные фильма некорректно заполнены!");
        }

        log.info("Обновление данных фильма {}", film.toString());
        idToFilms.put(film.getId(), film);
        return film;
    }

    private int generateId() {
        return id++;
    }
}
