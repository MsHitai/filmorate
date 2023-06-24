package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorDirectorException;
import ru.yandex.practicum.filmorate.exception.ErrorFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.ratingMpa.RatingMpaDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.EventsService.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final RatingMpaDao ratingMpa;
    private final LikeDao likeDao;
    private final UserService userService;
    private final GenreService genreService;
    private final DirectorStorage directorStorage;
    private final EventsService eventsService;

    public void addNewLike(long filmId, long userId) {
        contains(filmId);
        userService.findById(userId);
        likeDao.add(filmId, userId);
        eventsService.addEvent(userId, filmId, LIKE_TYPE, ADD_OPERATION);
    }

    public void removeLike(long filmId, long userId) {
        contains(filmId);
        userService.findById(userId);
        likeDao.delete(filmId, userId);
        eventsService.addEvent(userId, filmId, LIKE_TYPE, REMOVE_OPERATION);
    }

    public List<Film> getTopPopularFilms(int count, Integer genreId, Integer year) {
        List<Film> result = filmStorage.findAll().stream()
                .sorted(this::likeCompare)
                .limit(count).collect(Collectors.toCollection(LinkedList::new));

        result.forEach(film -> {
            film.setGenres(filmStorage.findGenres(film.getId()));
            film.setMpa(ratingMpa.findById(film.getMpa().getId()));
            film.setDirectors(filmStorage.findDirectors(film.getId()));
        });

        if (genreId == null && year == null) {
            return result;
        } else if (genreId != null && year == null) {
            genreService.findById(genreId);
            List<Film> sortedByGenre = new ArrayList<>();
            result.forEach(film -> film.getGenres().stream()
                    .filter(genre -> genre.getId() == genreId).map(genre -> film)
                    .forEach(sortedByGenre::add));
            return sortedByGenre;
        } else if (genreId == null) {
            return result.stream()
                    .filter(film -> film.getReleaseDate().getYear() == year)
                    .collect(Collectors.toList());
        } else {
            List<Film> sortedByBoth = new ArrayList<>();
            result.forEach(film -> film.getGenres().stream()
                    .filter(genre -> genre.getId() == genreId && film.getReleaseDate().getYear() == year)
                    .map(genre -> film).forEach(sortedByBoth::add));
            return sortedByBoth;
        }
    }

    public List<Film> getCommonPopularFilms(long userId, long friendId) {
        userService.findById(userId);
        userService.findById(friendId);

        List<Long> filmIdsByUserId = likeDao.getFilmIdLikes(userId);
        List<Long> filmIdsByFriendId = likeDao.getFilmIdLikes(friendId);
        List<Film> result = new ArrayList<>();

        for (Long filmId : filmIdsByFriendId) {
            if (filmIdsByUserId.contains(filmId)) {
                result.add(filmStorage.findById(filmId));
            }
        }

        result.stream()
                .sorted(this::likeCompare)
                .forEach(film -> {
                    film.setGenres(filmStorage.findGenres(film.getId()));
                    film.setMpa(ratingMpa.findById(film.getMpa().getId()));
                });

        return result;
    }

    public List<Film> findAll() {
        List<Film> films = filmStorage.findAll();
        films.forEach(film -> {
            film.setGenres(filmStorage.findGenres(film.getId()));
            film.setMpa(ratingMpa.findById(film.getMpa().getId()));
            film.setDirectors(filmStorage.findDirectors(film.getId()));
        });
        return films;
    }

    public Film create(Film film) {
        Film result = filmStorage.create(film);
        filmStorage.addGenres(result.getId(), film.getGenres());
        result.setGenres(filmStorage.findGenres(result.getId()));
        filmStorage.addDirectors(result.getId(), film.getDirectors());
        result.setDirectors(filmStorage.findDirectors(result.getId()));
        return result;
    }

    public Film update(Film film) {
        contains(film.getId());
        Film result = filmStorage.update(film);
        filmStorage.updateGenres(result.getId(), film.getGenres());
        result.setGenres(filmStorage.findGenres(result.getId()));
        result.setMpa(ratingMpa.findById(result.getMpa().getId()));
        filmStorage.updateDirectors(result.getId(), film.getDirectors());
        result.setDirectors(filmStorage.findDirectors(result.getId()));
        return result;
    }

    public Film findById(long filmId) {
        Film result = contains(filmId);
        result.setGenres(filmStorage.findGenres(filmId));
        result.setMpa(ratingMpa.findById(result.getMpa().getId()));
        result.setDirectors(filmStorage.findDirectors(filmId));
        return result;
    }

    public List<Film> getSortedDirectorFilms(long dirId, String sortBy) {
        if (directorStorage.findById(dirId) == null) {
            throw new ErrorDirectorException("Режиссёр не найден");
        }
        List<Film> directorFilms = new ArrayList<>();
        if (sortBy.equals("year")) {
            directorFilms = filmStorage.getAllDirectorFilms(dirId).stream()
                    .sorted(Comparator.comparing(Film::getReleaseDate))
                    .collect(Collectors.toList());
            directorFilms.forEach(film -> {
                film.setMpa(ratingMpa.findById(film.getMpa().getId()));
                film.setDirectors(filmStorage.findDirectors(film.getId()));
                film.setGenres(filmStorage.findGenres(film.getId()));
            });
        } else if (sortBy.equals("likes")) {
            directorFilms = filmStorage.getAllDirectorFilms(dirId).stream()
                    .sorted(this::likeCompare)
                    .collect(Collectors.toList());
            directorFilms.forEach(film -> {
                film.setMpa(ratingMpa.findById(film.getMpa().getId()));
                film.setDirectors(filmStorage.findDirectors(film.getId()));
                film.setGenres(filmStorage.findGenres(film.getId()));
            });
        }
        return directorFilms;
    }

    public void deleteFilm(long id) {
        contains(id);
        filmStorage.delete(id);
    }

    private int likeCompare(Film film, Film otherFilm) {
        return Integer.compare(likeDao.check(otherFilm.getId()), likeDao.check(film.getId()));
    }

    private Film contains(long filmId) {
        try {
            return filmStorage.findById(filmId);
        } catch (EmptyResultDataAccessException exception) {
            log.debug("Фильм с id {} не найден", filmId);
            throw new ErrorFilmException("Фильм не найден");
        }
    }

    public List<Film> search(String query, List<String> by) {

        try {
            return new ArrayList<>(filmStorage.searchFilms(query, by));
        } catch (EmptyResultDataAccessException exception) {
            log.debug("Фильм с id {} не найден", query);
            throw new ErrorFilmException("Фильм не найден");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
