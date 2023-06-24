package ru.yandex.practicum.filmorate.exception;

public class LoginAlreadyExistException extends RuntimeException {
    public LoginAlreadyExistException(String message) {
        super(message);
    }
}