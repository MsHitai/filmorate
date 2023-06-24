package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmService.findById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addNewLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addNewLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopPopularFilms(@RequestParam(defaultValue = "10") @Positive Integer count,
                                         @RequestParam(required = false, name = "genreId") Integer genreId,
                                         @RequestParam(required = false, name = "year") Integer year) {
        return filmService.getTopPopularFilms(count, genreId, year);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable long id) {
        filmService.deleteFilm(id);
    }

    @GetMapping("/common")
    public List<Film> getCommonPopularFilms(@RequestParam("userId") long userId,
                                            @RequestParam("friendId") long friendId) {
        return filmService.getCommonPopularFilms(userId, friendId);
    }

    @GetMapping("director/{id}")
    public List<Film> getSortedDirectorFilms(@PathVariable long id,
                                             @RequestParam(defaultValue = "year") @NotBlank String sortBy) {
        return filmService.getSortedDirectorFilms(id, sortBy);
    }

    @GetMapping("/search")
    public List<Film> getSearch(@RequestParam String query, @RequestParam List<String> by) {
        return filmService.search(query, by);
    }

}
