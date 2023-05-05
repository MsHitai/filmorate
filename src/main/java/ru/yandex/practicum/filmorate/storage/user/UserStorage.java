package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User addUser(User user);

    User deleteUser(int id);

    void deleteAll();

    User updateUser(User user);

    Collection<User> findAll();

    User findById(int id);
}
