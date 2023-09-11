package ru.yandex.practicum.filmorate.model;

public enum Rating {
    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R"),
    NC_17("NC-17");

    private final String rating;

    Rating(String rating) {
        this.rating = rating;
    }

    private String getRating() {
        return rating;
    }

    public static Rating getByName(String rating) {
        for (Rating current : Rating.values()) {
            if (current.getRating().equalsIgnoreCase(rating.trim())) {
                return current;
            }
        }
        return null;
    }

}
