package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorReviewException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.review.ReviewStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.EventsService.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserService userService;
    private final FilmService filmService;
    private final EventsService eventsService;

    public Review create(Review review) {
        userService.findById(review.getUserId());
        filmService.findById(review.getFilmId());

        Review review1 = reviewStorage.create(review);
        eventsService.addEvent(review1.getUserId(), review1.getReviewId(), REVIEW_TYPE, ADD_OPERATION);
        return review1;
    }

    public Review findById(long id) {
        try {
            return reviewStorage.findById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ErrorReviewException("Такого отзыва нет!");
        }
    }

    public List<Review> findAll(Long filmId, long count) {
        if (filmId == null) {
            return reviewStorage.findAll().stream()
                    .sorted(Comparator.comparingLong(Review::getUseful).reversed())
                    .limit(count)
                    .collect(Collectors.toList());
        }
        return reviewStorage.getAllReviewsByFilm(filmId).stream()
                .sorted(Comparator.comparingLong(Review::getUseful).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Review update(Review review) {
        Review review1 = findById(review.getReviewId());
        eventsService.addEvent(review1.getUserId(), review1.getReviewId(), REVIEW_TYPE, UPDATE_OPERATION);
        return reviewStorage.update(review);
    }

    public boolean delete(long id) {
        Review review = findById(id);
        eventsService.addEvent(review.getUserId(), review.getReviewId(), REVIEW_TYPE, REMOVE_OPERATION);
        return reviewStorage.delete(id);
    }

    public boolean addLike(long id, long userId) {
        userService.findById(userId);
        return reviewStorage.addLike(id, userId);
    }

    public boolean addDislike(long id, long userId) {
        return reviewStorage.addDislike(id, userId);
    }

    public boolean deleteLike(long id, long userId) {
        return reviewStorage.deleteLike(id, userId);
    }

    public boolean deleteDislike(long id, long userId) {
        return reviewStorage.deleteDislike(id, userId);
    }
}
