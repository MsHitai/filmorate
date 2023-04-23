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

class UserValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

    }

    @Test
    @DisplayName("Можно успешно создать пользователя с правильными полями")
    public void testUserCreateSuccess() {
        User user = User.builder()
                .name("vasya")
                .login("leilo")
                .birthday(LocalDate.of(2000, 10, 10))
                .email("mambaleilo@mail.ru")
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Если не указать логин, будет ошибка валидации")
    public void testValidationWhenNoLoginThenThrowException() {
        User user = new User(0, "mambaleilo@mail.ru", "", "vasya",
                LocalDate.of(2000, 10, 10));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Если указать пробел в логин, будет ошибка валидации")
    public void testValidationWhenLoginHasBlanksThenThrowException() {
        User user = new User(0, "mambaleilo@mail.ru", " ", "vasya",
                LocalDate.of(2000, 10, 10));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Если не указать имя - не будет ошибки валидации. Имя = логин")
    public void testValidationWhenNoNameSuccess() {
        User user = new User(0, "mambaleilo@mail.ru", "mambaleilo", "",
                LocalDate.of(2000, 10, 10));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
        assertEquals("mambaleilo", user.getName());
    }

    @Test
    @DisplayName("Если не указать email, будет ошибка валидации")
    public void testValidationWhenNoEmailThenThrowException() {
        User user = new User(0, "", "mambaleilo", "vasya",
                LocalDate.of(2000, 10, 10));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Если не указать @ в email, будет ошибка валидации")
    public void testValidationWhenNotCorrectEmailThenThrowException() {
        User user = new User(0, "yandex.mail.ru", "mambaleilo", "vasya",
                LocalDate.of(2000, 10, 10));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Если указать birthday в будущем, будет ошибка валидации")
    public void testValidationWhenBirthdayInFutureThenThrowException() {
        User user = new User(0, "mamboleilo@mail.ru", "mambaleilo", "vasya",
                LocalDate.of(2023, 10, 10));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}