package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository()
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    private int id;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sql = "INSERT INTO FRIENDS(FRIEND_ID, USER_ID, STATUS) VALUES(?, ?, 'ACCEPTED')";
        jdbcTemplate.update(sql, friendId, id);
    }

    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO USERS (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES (?, ?, ?, ?, ?)";
        user.setId(++id);
        String name = user.getName();

        if (name == null || name.isBlank() || name.isEmpty()) {
            user.setName(user.getLogin());
        }

        Object[] params = new Object[]{
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        };

        jdbcTemplate.update(sql, params);

        user.setFriends(getFriendsIds(user.getId()));

        return user;
    }

    @Override
    public void deleteUser(int id) {
        String sql = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        String sql = "DELETE FROM FRIENDS WHERE FRIEND_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, friendId, id);
    }

    @Override
    public Set<User> getFriends(int id) {
        String sql = "SELECT FRIEND_ID FROM FRIENDS " +
                "WHERE USER_ID = ? " +
                "GROUP BY FRIEND_ID " +
                "ORDER BY FRIEND_ID ASC";
        List<Friend> ids = jdbcTemplate.query(sql, this::friendMapRow, id);

        Set<User> friendUsers = new TreeSet<>(Comparator.comparingInt(User::getId));

        for (Friend friend : ids) {
            User user = findById(friend.getFriendId());
            friendUsers.add(user);
        }

        return friendUsers;
    }

    @Override
    public Collection<User> findAll() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, this::userMapRow);
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        User user = jdbcTemplate.query(sql, this::userMapRow, new Object[]{id})
                .stream().findAny().orElse(null);
        if (user == null) {
            throw new DataNotFoundException("Пользователя с таким id нет в базе");
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";

        Object[] params = new Object[]{
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        };

        jdbcTemplate.update(sql, params);
        return user;
    }

    private Set<Friend> getFriendsIds(int userId) {
        String sql = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ? GROUP BY FRIEND_ID";
        List<Friend> friends = jdbcTemplate.query(sql, this::friendMapRow, userId);
        TreeSet<Friend> sortedFriends = new TreeSet<>(Comparator.comparingInt(Friend::getFriendId));
        sortedFriends.addAll(friends);
        return sortedFriends;
    }

    private User userMapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder()
                .id(rs.getInt("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .build();
        user.setFriends(getFriendsIds(user.getId()));
        return user;
    }

    private Friend friendMapRow(ResultSet rs, int rowNum) throws SQLException {
        return Friend.builder()
                .friendId(rs.getInt("FRIEND_ID"))
                .build();
    }
}