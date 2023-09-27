package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public UserService(final UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addFriend(Long firstFriendId, Long secondFriendId) {
        return userStorage.addFriend(firstFriendId, secondFriendId);
    }

    public User deleteFriend(Long firstFriendId, Long secondFriendId) {
        return userStorage.deleteFriend(firstFriendId, secondFriendId);
    }

    public List<User> getUserFriends(Long id) {
        return userStorage.getUserFriends(id);
    }

    public List<User> getCommonFriends(Long firstFriendId, Long secondFriendId) {
        return userStorage.getCommonFriends(firstFriendId, secondFriendId);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }
}
