package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private int id;

    @Override
    public User addUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(int id) {
        User user = users.get(id);
        if (user == null) {
            throw new DataNotFoundException("Пользователя с таким идентификатором нет в базе");
        }
        return users.remove(id);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new DataNotFoundException("Пользователя с таким идентификатором нет в базе");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User findById(int id) {
        User user = users.get(id);
        if (user == null) {
            throw new DataNotFoundException("Пользователя с таким id нет в базе " + id);
        }
        return user;
    }
}
