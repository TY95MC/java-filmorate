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

    public void addFriend(int firstFriendId, int secondFriendId) {
//        userStorage.getUserById(firstFriendId).getFriends().add(secondFriendId);
//        userStorage.getUserById(secondFriendId).getFriends().add(firstFriendId);
        userStorage.addFriend(firstFriendId, secondFriendId);
    }

    public void deleteFriend(Integer firstFriendId, Integer secondFriendId) {
//        userStorage.getUserById(firstFriendId).getFriends().remove(secondFriendId);
//        userStorage.getUserById(secondFriendId).getFriends().remove(firstFriendId);
        userStorage.deleteFriend(firstFriendId, secondFriendId);
    }

    public List<User> getUserFriends(int id) {
//        return userStorage.getUserById(id).getFriends().stream()
//                .map(userStorage::getUserById)
//                .collect(Collectors.toUnmodifiableList());
        return userStorage.getUserFriends(id);
    }

    public List<User> getCommonFriends(int firstFriendId, int secondFriendId) {
//        return userStorage.getUserById(firstFriendId).getFriends().stream()
//                .filter(userStorage.getUserById(secondFriendId).getFriends()::contains)
//                .map(userStorage::getUserById)
//                .collect(Collectors.toUnmodifiableList());
        return userStorage.getCommonFriends(firstFriendId, secondFriendId);
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
