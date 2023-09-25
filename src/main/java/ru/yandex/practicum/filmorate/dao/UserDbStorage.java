package ru.yandex.practicum.filmorate.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Component("userDbStorage")
@Primary
public class UserDbStorage implements UserStorage {

    JdbcTemplate jdbc = new JdbcTemplate();

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    public void addFriend(int firstFriendId, int secondFriendId) {

    }

    public void deleteFriend(Integer firstFriendId, Integer secondFriendId) {

    }

    public List<User> getUserFriends(int id) {
        return new ArrayList<>();
    }

    public List<User> getCommonFriends(int firstFriendId, int secondFriendId) {
        return new ArrayList<>();
    }
}
