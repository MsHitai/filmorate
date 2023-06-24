package ru.yandex.practicum.filmorate.storage.dao.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final UserStorage userStorage;
    private User user;

    @BeforeEach
    public void beforeEach() {
        user = User.builder()
                .login("login")
                .name("name")
                .email("email@email.ru")
                .birthday(LocalDate.of(2000, 1, 11))
                .build();
    }

    @Test
    void findAllUsers() {
        List<User> users = new ArrayList<>(Collections.singleton(user));

        userStorage.create(user);
        List<User> usersActual = userStorage.findAll();

        assertThat(users.size(), is(1));
        assertThat(usersActual.size(), is(1));
    }

    @Test
    void createUser() {
        User userActual = userStorage.create(user);

        assertThat(userActual, not(nullValue()));
        assertThat(userActual.getName(), is("name"));
        assertThat(userActual.getLogin(), is("login"));
    }

    @Test
    void updateUser() {
        userStorage.create(user);
        user.setName("new Name");
        user.setLogin("newLogin");

        User userActual = userStorage.update(user);

        assertThat(userActual, not(nullValue()));
        assertThat(user.getName(), is("new Name"));
        assertThat(user.getLogin(), is("newLogin"));
    }

    @Test
    void findUserById() {
        userStorage.create(user);

        User userActual = userStorage.findById(1);

        assertThat(userActual, not(nullValue()));
        assertThat(userActual.getName(), is("name"));
        assertThat(userActual.getLogin(), is("login"));
    }
}