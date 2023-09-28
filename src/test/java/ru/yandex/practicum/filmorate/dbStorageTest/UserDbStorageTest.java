package ru.yandex.practicum.filmorate.dbStorageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDbStorageTest {

    private final UserDbStorage userStorage;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void fillDataBaseWithUsers() {
        user1 = new User("mail@yandex.ru", "login1", "Pirozhok",
                LocalDate.of(2001, 12, 12));
        user2 = new User("mail@yandex.ru", "login2", "MATb",
                LocalDate.of(1969, 5, 12));
        user3 = new User("mail@yandex.ru", "login3", "NeMATb",
                LocalDate.of(2068, 11, 12));
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addUser(user3);
    }

    @Test
    public void getUsersTest() {
        assertThat(userStorage.getUsers().size()).isEqualTo(3);
    }

    @Test
    public void addUserTestUnSuccessfully() {
        catchThrowableOfType(() -> userStorage.addUser(User.builder()
                .id(-1L)
                .email("mail@yandex.ru")
                .login("login1")
                .name("Pirozhok")
                .birthday(LocalDate.of(2001, 12, 12))
                .build()), EntityNotFoundException.class);
    }

    @Test
    public void updateUserTestUnSuccessfully() {
        catchThrowableOfType(() -> userStorage.updateUser(User.builder()
                .id(3L)
                .email("mail@yandex.ru")
                .login("login1")
                .name("Pirozhok")
                .birthday(LocalDate.of(2001, 12, 12))
                .build()), EntityNotFoundException.class);
    }

    @Test
    public void addFriendTestSuccessfully() {
        assertThat(userStorage.addFriend(user1.getId(), user2.getId()))
                .hasToString("User(id=1, email=mail@yandex.ru, login=login1, " +
                        "name=Pirozhok, birthday=2001-12-12, friends=[2])");
    }

    @Test
    public void deleteFriendTestSuccessfully() {
        userStorage.addFriend(user1.getId(), user2.getId());
        assertThat(userStorage.deleteFriend(user1.getId(), user2.getId()))
                .hasToString("User(id=1, email=mail@yandex.ru, login=login1, " +
                        "name=Pirozhok, birthday=2001-12-12, friends=[])");
    }

    @Test
    public void getUserFriendsTestSuccessfully() {
        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.addFriend(user1.getId(), user3.getId());
        assertThat(userStorage.getUserFriends(user1.getId()).size()).isEqualTo(2);
    }

    @Test
    public void getCommonFriendsTestSuccessfully() {
        userStorage.addFriend(user1.getId(), user2.getId());
        userStorage.addFriend(user1.getId(), user3.getId());
        userStorage.addFriend(user2.getId(), user3.getId());
        assertThat(userStorage.getCommonFriends(user1.getId(), user2.getId()).size()).isEqualTo(1);
    }

    @Test
    public void getUserByIdTestSuccessfully() {
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1L));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }
}
