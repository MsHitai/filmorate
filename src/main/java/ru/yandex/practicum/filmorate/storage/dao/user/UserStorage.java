package ru.yandex.practicum.filmorate.storage.dao.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.Storage;

public interface UserStorage extends Storage<User> {

    void delete(long userId);
}
