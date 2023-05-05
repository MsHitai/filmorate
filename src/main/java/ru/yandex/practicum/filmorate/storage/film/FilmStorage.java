package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film addFilm(Film film);

    Film deleteFilm(int id);

    void deleteAll();

    Film updateFilm(Film film);

    Collection<Film> findAll();

    Film findById(int number);
}
