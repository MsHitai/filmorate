package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review add(@Valid @RequestBody Review review) {
        return reviewService.create(review);
    }

    @GetMapping("/{id}")
    public Review get(@PathVariable long id) {
        return reviewService.findById(id);
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam(required = false) Long filmId,
                                   @RequestParam(defaultValue = "10") long count) {
        return reviewService.findAll(filmId, count);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable long id) {
        return reviewService.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean addLike(@PathVariable long id,
                           @PathVariable long userId) {
        return reviewService.addLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public boolean addDislike(@PathVariable long id,
                              @PathVariable long userId) {
        return reviewService.addDislike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean deleteLike(@PathVariable long id, @PathVariable long userId) {
        return reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public boolean deleteDislike(@PathVariable long id, @PathVariable long userId) {
        return reviewService.deleteDislike(id, userId);
    }
}
