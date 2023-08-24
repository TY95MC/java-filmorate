package ru.yandex.practicum.filmorate.controllerTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
                .hasCauseInstanceOf(ValidationException.class)
                .hasMessageContaining("Данные пользователя некорректно заполнены!");
    }
}
