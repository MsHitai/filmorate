package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        User user = userStorage.findById(id);
        user.addFriends(friendId);
        User user1 = userStorage.findById(friendId);
        user1.addFriends(id);
    }

    public Set<Integer> getFriends(int id) {
        User user = userStorage.findById(id);
        return user.getFriends();
    }

    public void deleteFriend(int id, int friendId) {
        User user = userStorage.findById(id);
        user.getFriends().remove(friendId);
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}
