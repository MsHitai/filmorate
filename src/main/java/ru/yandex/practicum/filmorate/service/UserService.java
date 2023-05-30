package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
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
        User user = findById(id);
        User friend = findById(friendId);
        userStorage.addFriend(user.getId(), friend.getId());
    }

    public Set<User> getFriends(int id) {
        return userStorage.getFriends(id);
    }

    public List<User> findCommonFriends(int userId, int otherId) {
        Set<User> friendsOtherIds = getFriends(otherId);

        Set<User> friendsIds = getFriends(userId);

        List<User> friends = new ArrayList<>();

        for (User friendsOtherId : friendsOtherIds) {
            if (friendsIds.contains(friendsOtherId)) {
                friends.add(friendsOtherId);
            }
        }

        return friends;
    }

    public void deleteFriend(int id, int friendId) {
        User user = findById(id);
        User friend = findById(friendId);
        userStorage.deleteFriend(user.getId(), friend.getId());
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User deleteUser(int id) {
        User user = findById(id);
        userStorage.deleteUser(user.getId());
        return user;
    }

    public User updateUser(User user) {
        findById(user.getId()); // проверяем на наличие в базе
        return userStorage.updateUser(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(int id) {
        return userStorage.findById(id);
    }
}
