package ru.yandex.practicum.filmorate.dbStorageTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private Film film1;
    private Film film2;
    private Film film3;
    private User user1;
    private User user2;

    @BeforeEach
    public void fillDataBaseWithFilms() {
        film1 = Film.builder()
                .name("SEVEN-1")
                .description("Бред Питт зря переехал с семьей")
                .releaseDate(LocalDate.of(1995, 9, 22))
                .duration(127)
                .likes(new HashSet<>())
                .genres(new HashSet<>(List.of(new Genre(2, "Драма"), new Genre(4, "Триллер"))))
                .mpa(new MpaRating(4, "R"))
                .build();
        film2 = Film.builder()
                .name("SEVEN-2")
                .description("Бред Питт зря переехал с семьей")
                .releaseDate(LocalDate.of(1995, 9, 22))
                .duration(127)
                .likes(new HashSet<>())
                .genres(new HashSet<>(List.of(new Genre(2, "Драма"), new Genre(4, "Триллер"))))
                .mpa(new MpaRating(4, "R"))
                .build();
        film3 = Film.builder()
                .name("SEVEN-3")
                .description("Бред Питт зря переехал с семьей")
                .releaseDate(LocalDate.of(1995, 9, 22))
                .duration(127)
                .likes(new HashSet<>())
                .genres(new HashSet<>(List.of(new Genre(2, "Драма"), new Genre(4, "Триллер"))))
                .mpa(new MpaRating(4, "R"))
                .build();
        filmDbStorage.addFilm(film1);
        filmDbStorage.addFilm(film2);
        filmDbStorage.addFilm(film3);
        user1 = new User("mail@yandex.ru", "login1", "Pirozhok",
                LocalDate.of(2001, 12, 12));
        user2 = new User("mail@yandex.ru", "login2", "MATb",
                LocalDate.of(1969, 5, 12));
        userDbStorage.addUser(user1);
        userDbStorage.addUser(user2);
    }

    @Test
    public void getFilms() {
        assertThat(filmDbStorage.getFilms().size()).isEqualTo(3);
    }

    @Test
    public void addFilmTestUnSuccessfully() {
        catchThrowableOfType(() -> filmDbStorage.addFilm(Film.builder()
                .id(1L)
                .name("SEVEN-1")
                .description("Бред Питт зря переехал с семьей Бред Питт зря переехал с семьей")
                .releaseDate(LocalDate.of(1995, 9, 22))
                .duration(127)
                .likes(new HashSet<>())
                .genres(new HashSet<>(List.of(new Genre(2, "Драма"), new Genre(4, "Триллер"))))
                .mpa(new MpaRating(4, "R")).build()), ValidationException.class);
    }

    @Test
    public void updateFilmTestUnSuccessfully() {
        catchThrowableOfType(() -> filmDbStorage.addFilm(Film.builder()
                .id(4L)
                .name("SEVEN")
                .description("Бред Питт зря переехал с семьей Бред Питт зря переехал с семьей")
                .releaseDate(LocalDate.of(1995, 9, 22))
                .duration(127)
                .likes(new HashSet<>())
                .genres(new HashSet<>(List.of(new Genre(2, "Драма"), new Genre(4, "Триллер"))))
                .mpa(new MpaRating(4, "R")).build()), EntityNotFoundException.class);
    }

    @Test
    public void getFilmByIdTestUnSuccessfully() {
        catchThrowableOfType(() -> filmDbStorage.addFilm(Film.builder()
                .id(777L)
                .name("SEVEN")
                .description("Бред Питт зря переехал с семьей Бред Питт зря переехал с семьей")
                .releaseDate(LocalDate.of(1995, 9, 22))
                .duration(127)
                .likes(new HashSet<>())
                .genres(new HashSet<>(List.of(new Genre(2, "Драма"), new Genre(4, "Триллер"))))
                .mpa(new MpaRating(4, "R")).build()), EntityNotFoundException.class);
    }

    @Test
    public void addLikeTestSuccessfully() {
        assertThat(filmDbStorage.addLike(film1.getId(), user1.getId()).getLikes()).isNotNull();
    }

    @Test
    public void deleteLikeTestSuccessfully() {
        filmDbStorage.addLike(film1.getId(), user1.getId());
        assertThat(filmDbStorage.deleteLike(film1.getId(), user1.getId()).getLikes().size()).isEqualTo(0);
    }

    @Test
    public void getPopularFilmsTestSuccessfully() {
        filmDbStorage.addLike(film1.getId(), user1.getId());
        filmDbStorage.addLike(film1.getId(), user2.getId());
        filmDbStorage.addLike(film2.getId(), user2.getId());
        assertThat(filmDbStorage.getPopularFilms(3L).size()).isEqualTo(3);
    }
}
