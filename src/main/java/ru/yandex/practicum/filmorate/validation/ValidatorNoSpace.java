package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ru.yandex.practicum.filmorate.validation.interfaces.NoSpace;

public class ValidatorNoSpace implements ConstraintValidator<NoSpace, String> {

    @Override
    public boolean isValid(String str, ConstraintValidatorContext context) {
        return str.matches("\\S+");
    }
}
