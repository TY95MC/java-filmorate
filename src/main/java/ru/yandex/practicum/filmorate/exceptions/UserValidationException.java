package ru.yandex.practicum.filmorate.exceptions;

public class UserValidationException extends RuntimeException {
    public UserValidationException() {
        super();
    }

    public UserValidationException(String message) {
        super(message);
    }

    public UserValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
