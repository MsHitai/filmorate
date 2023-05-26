package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
class FilmorateApplicationTests {

    private final UserStorage userStorage;

    private final FilmStorage filmStorage;

    private Film film;
    private Film film1;
    private User user;
    private User user1;

    @Autowired
    FilmorateApplicationTests(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    @BeforeEach
    public void setDataForStorage() {
        user = User.builder()
                .name("Johnny")
                .email("johnnymail@mail.ru")
                .login("mrJohnny")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        film = Film.builder()
                .name("Movie")
                .description("Movie Description")
                .duration(110)
                .releaseDate(LocalDate.of(1900, 1, 1))
                .mpa(Rating.builder()
                        .id(1)
                        .build())
                .genres(Set.of(Genre.builder().id(1).build(), Genre.builder().id(2).build()))
                .build();

        user1 = User.builder()
                .name("Johnny2")
                .email("johnnymail2@mail.ru")
                .login("mrJohnny2")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        film1 = Film.builder()
                .name("Movie2")
                .description("Movie2 Description")
                .duration(110)
                .releaseDate(LocalDate.of(1900, 1, 1))
                .mpa(Rating.builder()
                        .id(1)
                        .build())
                .genres(Set.of(Genre.builder().id(1).build(), Genre.builder().id(2).build()))
                .build();

        userStorage.addUser(user1);
        userStorage.addUser(user);
        filmStorage.addFilm(film);
        filmStorage.addFilm(film1);
    }

    @AfterEach
    public void deleteAll() {
        filmStorage.deleteFilm(film.getId());
        filmStorage.deleteFilm(film1.getId());
        userStorage.deleteUser(user1.getId());
        userStorage.deleteUser(user.getId());
    }

    @Test // тест на добавление пользователя
    public void testAddUser() {
        assertThat(user, not(nullValue()));
        assertThat(user.getName(), is("Johnny"));
        assertThat(user1, not(nullValue()));
        assertThat(user1.getName(), is("Johnny2"));
    }

    @Test // тест на добавление и получение друга через getFriends(id)
    public void testAddFriend() {

        userStorage.addFriend(user.getId(), user1.getId());

        List<User> friendsOfFirstUser = new ArrayList<>(userStorage.getFriends(user.getId()));

        assertThat(friendsOfFirstUser.size(), is(1));
        assertThat(friendsOfFirstUser.get(0).getId(), is(user1.getId()));

        List<User> friendsOfSecondUser = new ArrayList<>(userStorage.getFriends(user1.getId()));

        assertThat(friendsOfSecondUser.size(), is(0));
    }

    @Test // тест нахождения пользователя по id
    public void testFindUserById() {
        User user1 = userStorage.findById(user.getId());

        assertThat(user1, not(nullValue()));
    }

    @Test // тест обновления пользователя
    public void testUpdateUser() {
        User user2 = User.builder()
                .id(user.getId())
                .name("Johnny2")
                .email("johnnymail2@mail.ru")
                .login("mrJohnny2")
                .birthday(LocalDate.of(2002, 2, 2))
                .build();

        user = userStorage.updateUser(user2);

        assertThat(user.getName(), is("Johnny2"));
        assertThat(user.getEmail(), is("johnnymail2@mail.ru"));
        assertThat(user.getLogin(), is("mrJohnny2"));
        assertThat(user.getBirthday(), is(LocalDate.of(2002, 2, 2)));
    }

    @Test // тест метода нахождения всех пользователей
    public void testFindAllUsers() {
        assertThat(userStorage.findAll().size(), is(2));
    }

    @Test // тест метода нахождения общих друзей
    public void testFindCommonFriends() {
        User user2 = User.builder()
                .name("Johnny3")
                .email("johnnymail@mail.ru")
                .login("mrJohnny")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        userStorage.addUser(user2);

        userStorage.addFriend(user1.getId(), user.getId());
        userStorage.addFriend(user2.getId(), user.getId());

        List<User> commonFriend = userStorage.findCommonFriends(user1.getId(), user2.getId());

        assertThat(commonFriend.size(), is(1));
        assertThat(commonFriend.get(0), is(user));

        userStorage.deleteUser(user2.getId());
    }

    @Test // тест удаления друга
    public void testDeleteFriend() {
        userStorage.addFriend(user1.getId(), user.getId());

        assertThat(userStorage.getFriends(user1.getId()).size(), is(1));

        userStorage.deleteFriend(user1.getId(), user.getId());

        assertThat(userStorage.getFriends(user1.getId()).size(), is(0));
    }

    @Test // тест нахождения фильма по id
    public void testFindFilmById() {
        Film film2 = filmStorage.findById(film.getId());

        assertThat(film2, not(nullValue()));
    }

    @Test // тест на добавление фильма
    public void testAddFilm() {
        assertThat(film1, not(nullValue()));
        assertThat(film1.getId(), is(2));
        assertThat(film1.getName(), is("Movie2"));
    }

    @Test // тест обновления фильма
    public void testUpdateFilm() {
        Film film1 = Film.builder()
                .id(film.getId())
                .name("Movie2")
                .description("Movie2 Description")
                .duration(100)
                .releaseDate(LocalDate.of(1900, 1, 2))
                .mpa(Rating.builder()
                        .id(2)
                        .build())
                .genres(Set.of(Genre.builder().id(1).build()))
                .build();

        film = filmStorage.updateFilm(film1);

        assertThat(film.getName(), is("Movie2"));
        assertThat(film.getDescription(), is("Movie2 Description"));
        assertThat(film.getDuration(), is(100));
        assertThat(film.getReleaseDate(), is(LocalDate.of(1900, 1, 2)));
        assertThat(film.getMpa().getId(), is(2));
        assertThat(film.getGenres().size(), is(1));
    }

    @Test // тест метода нахождения всех фильмов
    public void testFindAllFilms() {
        assertThat(filmStorage.findAll().size(), is(2));
    }

    @Test // тест добавить лайки и найти популярные фильмы
    public void testAddLike() {
        filmStorage.addLike(film.getId(), user.getId());
        filmStorage.addLike(film.getId(), user1.getId());

        assertThat(filmStorage.findPopularFilms(10).size(), is(1));
    }

    @Test // тест удаления лайка
    public void testDeleteLike() {
        filmStorage.addLike(film.getId(), user.getId());
        filmStorage.addLike(film.getId(), user1.getId());

        assertThat(filmStorage.findPopularFilms(10).size(), is(1));

        filmStorage.deleteLike(film.getId(), user.getId());
        filmStorage.deleteLike(film.getId(), user1.getId());

        assertThat(filmStorage.findPopularFilms(10).size(), is(0));
    }

    @Test
    public void testFindAllGenres() {
        assertThat(filmStorage.findAllGenres().size(), is(6));
    }

    @Test
    public void testFindGenreById() {
        assertThat(filmStorage.findGenreById(2).getName(), is("Драма"));
    }

    @Test
    public void testFindAllRatings() {
        assertThat(filmStorage.findAllRatings().size(), is(5));
    }

    @Test
    public void testFindRatingById() {
        assertThat(filmStorage.findRatingById(2).getName(), is("PG"));
    }
}
