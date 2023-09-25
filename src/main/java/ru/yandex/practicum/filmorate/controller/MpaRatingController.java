package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
@RequestMapping("/mpa")
public class MpaRatingController {
    private final MpaRatingService ratingService;

    public MpaRatingController(final MpaRatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public List<MpaRating> getAllMpaRatings() {
        return ratingService.getAllMpaRatings();
    }

    @GetMapping("/{id}")
    public MpaRating getMpaRatingById(@PathVariable("id") @Min(1) int id) {
        return ratingService.getMpaRatingById(id);
    }
}
