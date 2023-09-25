package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(int id);

    void addFriend(int firstFriendId, int secondFriendId);

    void deleteFriend(Integer firstFriendId, Integer secondFriendId);

    List<User> getUserFriends(int id);

    List<User> getCommonFriends(int firstFriendId, int secondFriendId);
}
