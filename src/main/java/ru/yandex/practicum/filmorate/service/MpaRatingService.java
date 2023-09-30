package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;

@Service
public class MpaRatingService {
    private final MpaRatingStorage ratingStorage;

    public MpaRatingService(final MpaRatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public List<MpaRating> getAllMpaRatings() {
        return ratingStorage.findAllMpaRatings();
    }

    public MpaRating getMpaRatingById(int id) {
        return ratingStorage.findMpaRatingById(id);
    }
}
