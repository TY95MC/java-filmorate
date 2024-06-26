package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class Friendship {
    @Positive
    private int id;
    @NotBlank
    private String status;
}
