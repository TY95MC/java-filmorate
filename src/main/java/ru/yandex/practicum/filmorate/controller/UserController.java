package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable(value = "id", required = false) int userId,
                                       @PathVariable(value = "otherId", required = false) int otherId) {
        log.info("Получение списка общих друзей пользователей {} и {}",
                userService.getUserById(userId).toString(), userService.getUserById(otherId).toString());
        return userService.getCommonFriends(userId, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable(value = "id", required = false) int userId,
                          @PathVariable(value = "friendId", required = false) int friendId) {
        userService.addFriend(userId, friendId);
        log.info("Добавление в друзья пользователей {} и {}",
                userService.getUserById(userId), userService.getUserById(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable(value = "id", required = false) int userId,
                             @PathVariable(value = "friendId", required = false) int friendId) {
        log.info("Удаление из друзей пользователей {} и {}",
                userService.getUserById(userId).toString(), userService.getUserById(friendId).toString());
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable(value = "id", required = false) int id) {
        log.info("Получение списка друзей пользователя {}", userService.getUserById(id).toString());
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id", required = false) int userId) {
        log.info("Получение пользователя {}", userService.getUserById(userId).toString());
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping()
    public User create(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

}
