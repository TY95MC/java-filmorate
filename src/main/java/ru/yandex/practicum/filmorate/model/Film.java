package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {
    private int id;
    @NotBlank private String name;
    @Size(min = 1, max = 200) @NotBlank private String description;
    @MinimumDate private Date releaseDate;
    @Min(1) private int duration;
    private Set<Integer> likes;
    private Set<Integer> genres;
    private MpaRating mpa;
    //private int rate = likes.size();

    @JsonCreator
    public Film(String name, String description, Date releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

//    @JsonCreator
//    public Film(String name, String description, Date releaseDate, int duration, int mpa) {
//        this.name = name;
//        this.description = description;
//        this.releaseDate = releaseDate;
//        this.duration = duration;
//        this.mpa = mpa;
//    }


    public void setId(int id) {
        this.id = id;
    }
}
