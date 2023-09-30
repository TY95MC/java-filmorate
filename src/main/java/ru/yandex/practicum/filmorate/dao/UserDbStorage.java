package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component("userDbStorage")
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    public UserDbStorage(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        return List.copyOf(jdbcTemplate.query("SELECT * FROM users", new UserMapper()));
    }

    @Override
    public User addUser(User user) {
        String sqlUser = "INSERT INTO users(user_email, user_login, user_name, user_birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder id = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlUser, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, id);
        user.setId(Objects.requireNonNull(id.getKey()).longValue());
        return getUserById(user.getId());
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление пользователя id = {}", user.getId());
        checkIfUserExists(user.getId());
        String sqlUpdateUsersTable = "UPDATE users " +
                "SET user_email = ?, user_login = ?, user_name = ?, user_birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlUpdateUsersTable, user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday(), user.getId());

        if (user.getFriends() != null) {
            jdbcTemplate.update("DELETE FROM user_friendship WHERE user_id = " + user.getId());
            String sqlFriends = "INSERT INTO user_friendship(user_id, friend_id) VALUES(?, ?)";
            for (Long friend : user.getFriends()) {
                jdbcTemplate.update(sqlFriends, user.getId(), friend);
            }
        }
        return getUserById(user.getId());
    }

    @Override
    public User getUserById(Long id) {
        if (checkIfUserExists(id)) {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = " + id, new UserMapper());
        }
        return null;
    }

    public User addFriend(Long firstFriendId, Long secondFriendId) {
        log.info("Добавление пользователю id = {} друга {}", firstFriendId, secondFriendId);
        User user = getUserById(firstFriendId);
        checkIfUserExists(secondFriendId);
        user.getFriends().add(secondFriendId);
        return updateUser(user);
    }

    public User deleteFriend(Long firstFriendId, Long secondFriendId) {
        log.info("Удаление у пользователя id = {} друга {}", firstFriendId, secondFriendId);
        User user = getUserById(firstFriendId);
        checkIfUserExists(secondFriendId);
        user.getFriends().remove(secondFriendId);
        return updateUser(user);
    }

    public List<User> getUserFriends(Long id) {
        log.info("Получение всех друзей пользователя id = {}", id);
        String sql = "SELECT * FROM users AS u " +
                "LEFT OUTER JOIN USER_FRIENDSHIP AS uf ON uf.FRIEND_ID = u.USER_ID " +
                "WHERE uf.USER_ID = " + id;
        return List.copyOf(jdbcTemplate.query(sql, new UserMapper()));
    }

    public List<User> getCommonFriends(Long firstFriendId, Long secondFriendId) {
        log.info("Общие друзья пользователей {} и {}", firstFriendId, secondFriendId);
        String sql = "SELECT u.USER_ID, u.USER_EMAIL, u.USER_LOGIN, u.USER_NAME, u.USER_BIRTHDAY FROM users AS u " +
                "LEFT OUTER JOIN USER_FRIENDSHIP AS uf ON uf.FRIEND_ID = u.USER_ID " +
                "WHERE uf.USER_ID =  " + firstFriendId +
                " INTERSECT " +
                "SELECT u1.USER_ID, u1.USER_EMAIL, u1.USER_LOGIN, u1.USER_NAME, u1.USER_BIRTHDAY FROM users AS u1 " +
                "LEFT OUTER JOIN USER_FRIENDSHIP as uf1 ON uf1.FRIEND_ID = u1.USER_ID " +
                "WHERE uf1.USER_ID = " + secondFriendId;
        return List.copyOf(jdbcTemplate.query(sql, new UserMapper()));
    }

    protected boolean checkIfUserExists(Long id) {
        try {
            if (id < 1) {
                log.info("Некорректный идентификатор id:" + id);
                throw new EntityNotFoundException("Некорректный идентификатор id:" + id);
            }
            String sql = "SELECT COUNT(user_id) FROM users WHERE user_id = " + id;
            if (jdbcTemplate.queryForObject(sql, Integer.class) == 0) {
                log.info("Несуществующий идентификатор id:" + id);
                throw new EntityNotFoundException("Пользователь с id:" + id + " не найден.");
            }
            return true;
        } catch (EmptyResultDataAccessException e) {
            log.info("Некорректный идентификатор id:" + id);
            throw new EntityNotFoundException("Пользователя с id:" + id + " не существует.");
        }
    }

    private Set<Long> getIdSet(String sql) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        Set<Long> set = new HashSet<>();
        for (Map<String, Object> map : list) {
            for (Object tmp : map.values()) {
                String tmp1 = String.valueOf(tmp);
                set.add(Long.parseLong(tmp1));
            }
        }
        return set;
    }

    private class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getLong("user_id"))
                    .email(rs.getString("user_email"))
                    .login(rs.getString("user_login"))
                    .name(rs.getString("user_name"))
                    .birthday(rs.getDate("user_birthday").toLocalDate())
                    .friends(getIdSet("SELECT friend_id FROM user_friendship WHERE user_id = "
                            + rs.getLong("user_id")))
                    .build();
        }
    }
}
