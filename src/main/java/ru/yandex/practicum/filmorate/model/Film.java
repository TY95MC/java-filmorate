package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    @NotBlank
    private final String name;
    @Size(min = 1, max = 200)
    private final String description;
    @MinimumDate
    private final LocalDate releaseDate;
    @Min(1)
    private final int duration;
    private Set<Integer> likes;

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
