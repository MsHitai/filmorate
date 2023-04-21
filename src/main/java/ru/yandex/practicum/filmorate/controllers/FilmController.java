package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id;

    @GetMapping()
    public Collection<Film> findAll() {
        log.debug("Получен запрос GET");
        return films.values();
    }

    @PostMapping()
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST на создание фильма {}", film.toString());
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT на обновление фильма {}", film.toString());
        if(!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с таким идентификатором нет в базе");
        }
        films.put(film.getId(), film);
        return film;
    }
}
