package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ErrorGenreException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.genre.GenreDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDao genreDao;

    public Genre findById(int genreId) {
        contains(genreId);
        return genreDao.findById(genreId);
    }

    public List<Genre> findAll() {
        return genreDao.findAll();
    }

    private void contains(int genreId) {
        try {
            genreDao.findById(genreId);
        } catch (EmptyResultDataAccessException exception) {
            log.debug("Жанр с id {} не найден", genreId);
            throw new ErrorGenreException("Жанр не найден");
        }
    }
}
