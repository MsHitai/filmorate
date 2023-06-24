package ru.yandex.practicum.filmorate.storage.dao.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private Film film;
    private final Mpa mpa = new Mpa();

    @BeforeEach
    public void beforeEach() {
        mpa.setId(1);
        film = Film.builder()
                .id(1)
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 11))
                .duration(60)
                .mpa(mpa)
                .build();
    }

    @Test
    void findAll() {
        List<Film> films = new ArrayList<>(Collections.singleton(film));
        filmDbStorage.create(film);
        List<Film> filmsActual = filmDbStorage.findAll();

        assertEquals(films, filmsActual);
    }

    @Test
    void create() {
        Film filmActual = filmDbStorage.create(film);

        assertEquals(film, filmActual);
    }

    @Test
    void update() {
        filmDbStorage.create(film);
        film.setDuration(90);
        film.setName("new Name");
        Film filmActual = filmDbStorage.update(film);

        assertEquals(film, filmActual);
    }

    @Test
    void findById() {
        filmDbStorage.create(film);

        Film filmActual = filmDbStorage.findById(1);

        assertEquals(film, filmActual);
    }


}