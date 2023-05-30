package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film deleteFilm(int id);

    Film updateFilm(Film film);

    Collection<Film> findAll();

    Film findById(int number);

    void addLike(int filmId, int userId);

    List<Film> findPopularFilms(int size);

    void deleteLike(int filmId, int userId);

    List<Genre> findAllGenres();

    Genre findGenreById(int genreId);

    List<Rating> findAllRatings();

    Rating findRatingById(int id);
}
