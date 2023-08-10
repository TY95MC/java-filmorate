package ru.yandex.practicum.filmorate.exceptions;

public class FilmValidationException extends RuntimeException {
    public FilmValidationException() {
        super();
    }

    public FilmValidationException(String message) {
        super(message);
    }

    public FilmValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
