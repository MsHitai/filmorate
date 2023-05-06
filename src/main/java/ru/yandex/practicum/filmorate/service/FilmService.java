package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.findById(filmId);
        film.addLike(userId);
    }

    public List<Film> findPopularFilms(int size) {
        Collection<Film> likes = filmStorage.findAll();
        return likes.stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(size)
                .collect(Collectors.toList());
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.findById(filmId);
        film.getLikes().remove(userId);
    }

    public FilmStorage getFilmStorage() {
        return filmStorage;
    }
}
