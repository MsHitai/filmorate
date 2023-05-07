package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable(required = false) String id) {
        if (id != null) {
            int number = Integer.parseInt(id);
            User user = userService.findById(number);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь по этому id не найден.");
            } else {
                return user;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable("id") int id) {
        Set<Integer> friendsIds = userService.getFriends(id);
        List<User> friends = new ArrayList<>();
        for (Integer idNum : friendsIds) {
            User user = userService.findById(idNum);
            friends.add(user);
        }
        return friends.stream()
                .sorted((o1, o2) -> Integer.compare(o1.getId(), o2.getId()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        Set<Integer> friendsOtherIds = userService.getFriends(otherId);

        Set<Integer> friendsIds = userService.getFriends(id);

        List<User> friends = new ArrayList<>();

        for (Integer friendsOtherId : friendsOtherIds) {
            if (friendsIds.contains(friendsOtherId)) {
                User user = userService.findById(friendsOtherId);
                friends.add(user);
            }
        }

        return friends;
    }


    @PostMapping()
    public User addUser(@Valid @RequestBody User user) {
        log.debug("Получен запрос POST на создание пользователя {}", user.toString());
        return userService.addUser(user);
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Получен запрос PUT на обновление пользователя {}", user.toString());
        user = userService.updateUser(user);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя с таким идентификатором нет в базе");
        }
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.debug("Получен запрос PUT на добавление друга по id {}", friendId);
        User user = userService.findById(friendId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя с таким идентификатором нет в базе");
        }
        User user1 = userService.findById(id);
        if (user1 == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя с таким идентификатором нет в базе");
        }
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable(required = false) Integer id) {
        log.debug("Получен запрос DELETE для пользователя по id {}", id);
        if (id == null) {
            userService.deleteAll();
        } else {
            User user = userService.deleteUser(id);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя с таким идентификатором нет в базе");
            }
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.debug("Получен запрос DELETE друга по id {}", friendId);
        userService.deleteFriend(id, friendId);
    }
}
