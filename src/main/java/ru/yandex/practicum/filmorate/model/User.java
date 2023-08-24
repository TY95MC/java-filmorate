package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    @NotNull
    private int id;
    @Email
    @NotBlank
    private final String email;
    @NotBlank
    @NotNull
    @NotBlank
    private final String login;
    private String name;
    @NotNull
    @PastOrPresent
    private final LocalDate birthday;
    private List<Integer> friends;

    @JsonCreator
    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;

        if (name == null || name.isBlank()) {
            this.name = login;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
