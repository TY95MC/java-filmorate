package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private final UserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void addFriend(int firstFriendId, int secondFriendId) {
        if (firstFriendId > 0 && secondFriendId > 0) {
            userStorage.getUserById(firstFriendId).getFriends().add(secondFriendId);
            userStorage.getUserById(secondFriendId).getFriends().add(firstFriendId);
        } else {
            throw new UserNotFoundException("Введен неверный идентификатор!");
        }
    }

    public void deleteFriend(Integer firstFriendId, Integer secondFriendId) {
        if (firstFriendId > 0 && secondFriendId > 0) {
            userStorage.getUserById(firstFriendId).getFriends().remove(secondFriendId);
            userStorage.getUserById(secondFriendId).getFriends().remove(firstFriendId);
        } else {
            throw new UserNotFoundException("Введен неверный идентификатор!");
        }
    }

    public List<User> getUserFriends(int id) {
        return userStorage.getUserById(id).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<User> getCommonFriends(int firstFriendId, int secondFriendId) {
        return userStorage.getUserById(firstFriendId).getFriends().stream()
                .filter(userStorage.getUserById(secondFriendId).getFriends()::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toUnmodifiableList());
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }
}
