package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    private int id;

    @GetMapping()
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST на создание пользователя {}", user.toString());
        //String name = user.getName();
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping()
    public User updateFilm(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT на обновление пользователя {}", user.toString());
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователя с таким идентификатором нет в базе");
        }
        users.put(user.getId(), user);
        return user;
    }
}
