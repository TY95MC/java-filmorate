package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Film {
    @NotNull
    private int id;
    @NotNull
    @NotBlank
    private final String name;
    @NotNull
    @Size(min = 1, max = 200)
    private final String description;
    @NotNull
    @MinimumDate
    private final LocalDate releaseDate;
    @NotNull
    @Min(1)
    private final int duration;
    private List<Integer> likes = new ArrayList<>();

    @JsonCreator
    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void setId(int id) {
        this.id = id;
    }
}
