package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public Collection<User> findAll() {
        log.debug("Получен запрос GET на получение всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable(required = false) int id) {
        log.debug("Получен запрос GET на получение пользователя по id {}", id);
        return userService.findById(id);
    }

    @GetMapping("/{id}/friends")
    public Set<User> findFriends(@PathVariable("id") int id) {
        log.debug("Получен запрос GET на получение друзей пользователя по id {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        log.debug("Получен запрос GET на получение общих друзей, id первого пользователя {} и " +
                "id второго пользователя {}", id, otherId);
        return userService.findCommonFriends(id, otherId);
    }


    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST на создание пользователя {}", user.toString());
        return userService.addUser(user);
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT на обновление пользователя {}", user.toString());
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.debug("Получен запрос PUT на добавление друга по id {}", friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(required = false) Integer id) {
        log.debug("Получен запрос DELETE для пользователя по id {}", id);
        userService.deleteUser(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.debug("Получен запрос DELETE друга по id {}", friendId);
        userService.deleteFriend(id, friendId);
    }
}
