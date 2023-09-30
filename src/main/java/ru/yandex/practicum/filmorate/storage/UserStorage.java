package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    User addFriend(Long firstFriendId, Long secondFriendId);

    User deleteFriend(Long firstFriendId, Long secondFriendId);

    List<User> getUserFriends(Long id);

    List<User> getCommonFriends(Long firstFriendId, Long secondFriendId);
}
