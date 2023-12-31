package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);
    private final Map<Integer, User> idToUser = new HashMap<>();
    private int id = 1;

    public List<User> getUsers() {
        log.info("Количество пользователей в текущий момент: {}.", idToUser.size());
        return List.copyOf(idToUser.values());
    }

    @Override
    public User addUser(User user) {
        if (!idToUser.containsKey(user.getId())) {
            user.setId(generateId());
            user.setFriends(new HashSet<>());
            idToUser.put(user.getId(), user);
            log.info("Сохраняется пользователь: {}.", user.toString());
            return user;
        } else {
            log.info("Попытка зарегистрировать уже существующего пользователя {}.", user.toString());
            throw new ValidationException("Ошибка! Такой пользователь уже существует!");
        }
    }

    @Override
    public User updateUser(User user) {
        if (!idToUser.containsKey(user.getId())) {
            log.info("Попытка обновить несуществующего пользователя {}.", user.toString());
            throw new EntityNotFoundException("Ошибка! Такого пользователя не существует!");
        } else {
            if (user.getFriends() == null) {
                user.setFriends(idToUser.get(user.getId()).getFriends());
            }
            log.info("Обновление данных пользователя {}", user.toString());
            idToUser.put(user.getId(), user);
            return user;
        }
    }

    @Override
    public User getUserById(int id) {
        if (!idToUser.containsKey(id)) {
            log.info("Попытка получить несуществующего пользователя с идентификатором {}.", id);
            throw new EntityNotFoundException("Ошибка! Пользователя с таким идентификатором нет!");
        } else {
            log.info("Получение по id данных пользователя {}.", idToUser.get(id).toString());
            return idToUser.get(id);
        }
    }

    private int generateId() {
        return id++;
    }

}
