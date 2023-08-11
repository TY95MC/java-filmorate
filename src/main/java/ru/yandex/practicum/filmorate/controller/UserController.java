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
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static int id = 1;

    private final Map<Integer, User> idToUsers = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> getUsers() {
        log.info("Количество пользователей в текущий момент: {}", idToUsers.size());
        return List.copyOf(idToUsers.values());
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        if (idToUsers.containsKey(user.getId())) {
            log.info("Попытка зарегистрировать уже существующего пользователя {}", user.toString());
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }

        user.setId(generateId());
        log.info("Сохраняется пользователь: {}", user.toString());
        idToUsers.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!idToUsers.containsKey(user.getId())) {
            log.info("Попытка обновить несуществующего пользователя {}", user.toString());
            throw new ValidationException("Данные пользователя некорректно заполнены!");
        }

        log.info("Обновление данных пользователя {}", user.toString());
        idToUsers.put(user.getId(), user);
        return user;
    }

    private int generateId() {
        return id++;
    }

}
