package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class RatingMpaController {
    private final RatingMpaService ratingMpaService;

    @GetMapping
    public List<Mpa> findAll() {
        return ratingMpaService.findAll();
    }

    @GetMapping("/{id}")
    public Mpa findById(@PathVariable int id) {
        return ratingMpaService.findById(id);
    }
}
