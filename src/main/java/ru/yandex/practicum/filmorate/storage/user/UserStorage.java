package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserStorage {

    User addUser(User user);

    void addFriend(int id, int friendId);

    User deleteUser(int id);

    Set<User> getFriends(int id);

    User updateUser(User user);

    Collection<User> findAll();

    User findById(int id);

    List<User> findCommonFriends(int userId, int otherId);

    void deleteFriend(int id, int friendId);
}
