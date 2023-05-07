package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public Collection<Film> findAll() {
        log.debug("Получен запрос GET");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable(required = false) String id) {
        if (id != null) {
            int number = Integer.parseInt(id);
            Film film = filmService.findById(number);
            if (film == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            } else {
                return film;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/popular")
    public List<Film> findPopular(@RequestParam(defaultValue = "10", required = false) int count) {
        return filmService.findPopularFilms(count);
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST на создание фильма {}", film.toString());
        return filmService.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT на обновление фильма {}", film.toString());
        film = filmService.updateFilm(film);
        if (film == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильма с таким идентификатором нет в базе");

        }
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void userAddLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.debug("Получен запрос на добавление лайка от пользователя по id {}", userId);
        Film film = filmService.findById(id);
        if (film == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильма с таким идентификатором нет в базе");

        }
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable(required = false) Integer id) {
        log.debug("Получен запрос DELETE");
        if (id == null) {
            filmService.deleteAll();
        } else {
            Film film = filmService.deleteFilm(id);
            if (film == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с этим идентификатором не найден");
            }
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.debug("Получен запрос на удаление лайка от пользователя по id {}", userId);
        Film film = filmService.findById(id);
        if (!film.getLikes().contains(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с этим ид не ставил лайк этому фильму");
        }
        filmService.deleteLike(id, userId);
    }
}
