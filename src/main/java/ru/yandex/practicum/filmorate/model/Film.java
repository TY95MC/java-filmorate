package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {
    private long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    @NotBlank
    private String description;
    @MinimumDate
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    private Set<Long> likes;
    private Set<Genre> genres;
    private MpaRating mpa;
}
