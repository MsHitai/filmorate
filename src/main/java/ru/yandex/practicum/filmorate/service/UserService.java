package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        userStorage.addFriend(id, friendId);
    }

    public Set<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    public List<User> findCommonFriends(int userId, int otherId) {
        return userStorage.findCommonFriends(userId, otherId);
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User deleteUser(int id) {
        return userStorage.deleteUser(id);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(int id) {
        return userStorage.findById(id);
    }
}
