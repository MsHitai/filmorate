package ru.yandex.practicum.filmorate.exception;

public class ErrorUserException extends RuntimeException {
    public ErrorUserException(String message) {
        super(message);
    }
}
