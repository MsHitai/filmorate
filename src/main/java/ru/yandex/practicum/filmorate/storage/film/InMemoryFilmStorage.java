package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id;

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteFilm(int id) {
        Film film = films.get(id);
        if (film == null) {
            throw new DataNotFoundException("Фильма с таким идентификатором нет в базе");
        }
        return films.remove(id);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new DataNotFoundException("Фильма с таким идентификатором нет в базе");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findById(int id) {
        final Film film = films.get(id);
        if (film == null) {
            throw new DataNotFoundException("Id не найден, id = " + id);
        }
        return film;
    }

    @Override
    public void addLike(int filmId, int userId) {

    }

    @Override
    public List<Film> findPopularFilms(int size) {
        return null;
    }

    @Override
    public void deleteLike(int filmId, int userId) {

    }

    @Override
    public List<Genre> findAllGenres() {
        return null;
    }

    @Override
    public Genre findGenreById(int filmId) {
        return null;
    }

    @Override
    public List<Rating> findAllRatings() {
        return null;
    }

    @Override
    public Rating findRatingById(int id) {
        return null;
    }
}
