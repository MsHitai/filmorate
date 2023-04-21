package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Можно успешно создать фильм с правильными полями")
    public void testFilmCreateSuccess() {
        Film film = new Film(0, "catzilla", "film about a great white cat",
                LocalDate.of(2023, 1, 10), 120, 10);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Если не указать название, будет ошибка валидации")
    public void testValidationWhenNoNameThenThrowException() {
        Film film = new Film(0, "", "film about a great white cat",
                LocalDate.of(2023, 1, 10), 120, 10);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Если сделать слишком длинное описание, будет ошибка валидации")
    public void testValidationWhenDescriptionTooLongThenThrowException() {
        Film film = new Film(0, "catzilla", "film about a great white cat, where she walks around " +
                "the New York city and crushes all buildings. She is very big. Really, like a godzilla. Then, a dog " +
                "comes in the picture and here begins all the fun. This is very good, really, try it!",
                LocalDate.of(2023, 1, 10), 120, 10);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Если указать дату релиза раньше 28.12.1895, будет ошибка валидации")
    public void testValidationWhenReleaseDateBeforeFilmBirthdayThenThrowException() {
        Film film = new Film(0, "catzilla", "film about a great white cat",
                LocalDate.of(1895, 1, 10), 120, 10);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Если указать отрицательное значение у продолжительности, будет ошибка валидации")
    public void testValidationWhenNegativeDurationThenThrowException() {
        Film film = new Film(0, "catzilla", "film about a great white cat",
                LocalDate.of(2023, 1, 10), -120, 10);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}