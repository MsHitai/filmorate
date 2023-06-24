package ru.yandex.practicum.filmorate.storage.dao.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.LinkedList;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre findById(int genreId) {
        Genre genre = jdbcTemplate.queryForObject(format(""
                + "SELECT genre_id, name "
                + "FROM genres "
                + "WHERE genre_id=%d", genreId), new GenreMapper());
        log.debug("Возвращён жанр: {}.", genre);
        return genre;
    }

    @Override
    public List<Genre> findAll() {
        List<Genre> result = new LinkedList<>(jdbcTemplate.query("SELECT genre_id, name "
                + "FROM genres "
                + "ORDER BY genre_id", new GenreMapper()));
        log.trace("Все жанры: {}.", result);
        return result;
    }
}
