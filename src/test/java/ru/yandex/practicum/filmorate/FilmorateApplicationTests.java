package ru.yandex.practicum.filmorate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAddFilmAndUpdateFilm() throws Exception {
        //Добавление фильма
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Film name\", \"description\": \"Film description\"," +
                                " \"releaseDate\": \"1967-03-25\", \"duration\": 100}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films"))
                .andExpect(content().string("[{\"name\":\"Film name\",\"description\":\"Film description\"," +
                        "\"releaseDate\":\"1967-03-25\",\"duration\":100,\"id\":1}]"));

        //Обновление фильма
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": \"1\",\"name\": \"FilmNameUpdate\", \"description\": \"Film description\"," +
                                " \"releaseDate\": \"1967-03-25\", \"duration\": 100}"))
                .andExpect(status().isOk());/* */

        mockMvc.perform(get("/films"))
                .andExpect(content().string("[{\"name\":\"FilmNameUpdate\",\"description\":\"Film description\"," +
                        "\"releaseDate\":\"1967-03-25\",\"duration\":100,\"id\":1}]"));
    }

    @Test
    void shouldAddAndUpdateUserSuccessfully() throws Exception {
        //пользователь с пустым именем
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"dolore\", \"name\": \"\", \"email\": \"mail@mail.ru\"," +
                                " \"birthday\": \"1946-08-20\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users"))
                .andExpect(content().string("[{\"email\":\"mail@mail.ru\",\"login\":\"dolore\"," +
                        "\"name\":\"dolore\",\"birthday\":\"1946-08-20\",\"id\":1}]"));

        //Обновление пользователя
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"mail@mail.ru\",\"login\":\"dolore\",\"name\":\"doloreUpdate\"," +
                                "\"birthday\":\"1946-08-20\",\"id\":1}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users"))
                .andExpect(content().string("[{\"email\":\"mail@mail.ru\",\"login\":\"dolore\"," +
                        "\"name\":\"doloreUpdate\",\"birthday\":\"1946-08-20\",\"id\":1}]"));
    }

    @Test
    void shouldAddFilmAndUpdateFilmUnsuccessfully() throws Exception {
        //фильм с пустым названием
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"description\": \"Film description\"," +
                                " \"releaseDate\": \"1967-03-25\", \"duration\": 100}"))
                .andExpect(status().is4xxClientError());

        //фильм со слишком длинным описанием
        Assertions.assertThatThrownBy(
                        () -> mockMvc.perform(MockMvcRequestBuilders.post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\": \"FilmName\", \"description\": \"Film descriptionFilm descriptionFilm " +
                                        "descriptionFilm descriptionFilm descriptionFilm descriptionFilm descriptionFilm " +
                                        "descriptionFilm descriptionFilm descriptionFilm descriptionFilm descriptionFilm " +
                                        "descriptionFilm descriptionFilm descriptionFilm descriptionFilm descriptionFilm " +
                                        "descriptionFilm descriptionFilm description Film descriptionFilm descriptionFilm " +
                                        "descriptionFilm descriptionFilm descriptionFilm descriptionFilm description\"," +
                                        " \"releaseDate\": \"1967-03-25\", \"duration\": 100}")))
                .hasCauseInstanceOf(FilmValidationException.class)
                .hasMessageContaining("Данные фильма некорректно заполнены!");

        //обновление несуществующего фильма
        Assertions.assertThatThrownBy(
                        () -> mockMvc.perform(put("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"id\": \"2\",\"name\": \"FilmNameUpdate\", \"description\": \"Film description\"," +
                                        " \"releaseDate\": \"1967-03-25\", \"duration\": 100}")))
                .hasCauseInstanceOf(FilmValidationException.class)
                .hasMessageContaining("Данные фильма некорректно заполнены!");

        //фильм с неправильной датой выпуска
        Assertions.assertThatThrownBy(
                        () -> mockMvc.perform(put("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\": \"FilmName\", \"description\": \"Film description\"," +
                                        " \"releaseDate\": \"1890-03-25\", \"duration\": 100}")))
                .hasCauseInstanceOf(FilmValidationException.class)
                .hasMessageContaining("Данные фильма некорректно заполнены!");
    }

    @Test
    void shouldAddUserAndUpdateUserUnsuccessfully() throws Exception {
        //пользователь с неправильным email
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\": \"dolore\",\"email\": \"mailmail.ru\"," +
                        " \"birthday\": \"1946-08-20\"}")).andExpect(status().is4xxClientError());

        //обновление несуществующего пользователя
        Assertions.assertThatThrownBy(
                        () -> mockMvc.perform(put("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"email\":\"mail@mail.ru\",\"login\":\"dolore\",\"name\":\"Nick Name\"," +
                                        "\"birthday\":\"1946-08-20\"}")))
                .hasCauseInstanceOf(UserValidationException.class)
                .hasMessageContaining("Данные пользователя некорректно заполнены!");
    }

}
