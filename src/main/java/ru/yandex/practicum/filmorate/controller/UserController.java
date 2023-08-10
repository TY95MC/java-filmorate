package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static int id = 1;

    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Количество пользователей в текущий момент: {}", users.size());
        return Collections.unmodifiableCollection(users.values());
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            log.info("Попытка зарегистрировать уже существующего пользователя {}", user.toString());
            throw new UserValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }

        if (!isValidUser(user)) {
            log.info("Попытка добавить некорректного пользователя {}", user.toString());
            throw new UserValidationException("Данные пользователя некорректно заполнены!");
        } else {
            user.setId(generateId());
            log.info("Сохраняется пользователь: {}", user.toString());
            users.put(user.getId(), user);
            return user;
        }
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Попытка обновить несуществующего пользователя {}", user.toString());
            throw new UserValidationException("Данные пользователя некорректно заполнены!");
        }

        if (!isValidUser(user)) {
            log.info("Попытка обновить некорректного пользователя {}", user.toString());
            throw new UserValidationException("Данные пользователя некорректно заполнены!");
        }

        log.info("Обновление данных пользователя {}", user.toString());
        users.put(user.getId(), user);
        return user;
    }

    private boolean isValidUser(User user) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,7}$";
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user.getEmail().contains("@")
                && !user.getEmail().isBlank()
                && !user.getLogin().isBlank()
                && user.getBirthday().isBefore(LocalDate.now().minus(Period.ofYears(14)));
    }

    private int generateId() {
        return id++;
    }

}
