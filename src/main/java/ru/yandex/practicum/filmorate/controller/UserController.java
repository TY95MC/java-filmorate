package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Long userId,
                                       @PathVariable("otherId") Long otherId) {
        log.info("Получение списка общих друзей пользователей {} и {}.", userId, otherId);
        return userService.getCommonFriends(userId, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Long userId,
                          @PathVariable("friendId") Long friendId) {
        log.info("Добавление в друзья пользователей {} и {}.", userId, friendId);
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") Long userId,
                             @PathVariable("friendId") Long friendId) {
        log.info("Удаление из друзей пользователей {} и {}.", userId, friendId);
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") Long id) {
        log.info("Получение списка друзей пользователя {}.", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long userId) {
        log.info("Получение пользователя {}.", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }
}
