package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorRatingMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.ratingMpa.RatingMpaDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingMpaService {
    private final RatingMpaDao ratingMpaDao;

    public Mpa findById(int ratingId) {
        contains(ratingId);
        return ratingMpaDao.findById(ratingId);
    }

    public List<Mpa> findAll() {
        return ratingMpaDao.findAll();
    }

    private void contains(int ratingId) {
        try {
            ratingMpaDao.findById(ratingId);
        } catch (EmptyResultDataAccessException exception) {
            log.debug("Рейтинг MPA с id {} не найден", ratingId);
            throw new ErrorRatingMpaException("Рейтинг MPA не найден");
        }
    }
}
