package ru.yandex.practicum.filmorate.storage.dao.friends;

import ru.yandex.practicum.filmorate.model.Friends;

import java.util.Set;

public interface FriendsDao {
    void add(long userId, long friendId, boolean isMutual);

    void delete(long userId, long friendId);

    Set<Long> getFriends(long userId);

    Friends get(long userId, long friendId);
}
