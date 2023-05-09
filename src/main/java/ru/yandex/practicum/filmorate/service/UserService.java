package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        User user = userStorage.findById(id);
        User user1 = userStorage.findById(friendId);
        user.addFriends(friendId);
        user1.addFriends(id);
    }

    public List<User> getFriends(int id) {
        User user = userStorage.findById(id);
        Set<Integer> friendsIds = user.getFriends();
        List<User> friends = new ArrayList<>();
        for (Integer idNum : friendsIds) {
            User user1 = userStorage.findById(idNum);
            friends.add(user1);
        }
        return friends.stream()
                .sorted(Comparator.comparingInt(User::getId))
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriends(int userId, int otherId) {
        List<User> friendsOtherIds = getFriends(otherId);

        List<User> friendsIds = getFriends(userId);

        List<User> friends = new ArrayList<>();

        for (User friendsOtherId : friendsOtherIds) {
            if (friendsIds.contains(friendsOtherId)) {
                friends.add(friendsOtherId);
            }
        }

        return friends;
    }

    public void deleteFriend(int id, int friendId) {
        User user = userStorage.findById(id);
        user.getFriends().remove(friendId);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User deleteUser(int id) {
        return userStorage.deleteUser(id);
    }

    public void deleteAll() {
        userStorage.deleteAll();
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
