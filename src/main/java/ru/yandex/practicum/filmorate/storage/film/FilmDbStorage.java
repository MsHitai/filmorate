package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository()
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private int id;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO FILMS (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        film.setId(++id);
        Object[] params = new Object[]{
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId()
        };
        jdbcTemplate.update(sql, params);

        updateGenres(film);

        return film;
    }

    @Override
    public void addLike(int filmId, int userId) {
        Film film = findById(filmId); // проверяем на наличие в базе
        String sql = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        film.setLikes(getUserLikes(filmId)); //обновляем лайки у фильма
    }

    @Override
    public Film deleteFilm(int id) {
        Film film = findById(id);
        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
        film.setLikes(getUserLikes(id)); //обновляем лайки у фильма
        return film;
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        findById(filmId);
        String sql = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        int result = jdbcTemplate.update(sql, filmId, userId);
        if (result == 0) {
            throw new DataNotFoundException("Пользователь с этим ид не ставил лайк этому фильму");
        }
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT F.* FROM FILMS F";
        return jdbcTemplate.query(sql, (this::filmMapRow));
    }

    @Override
    public List<Genre> findAllGenres() {
        String sql = "SELECT G.* FROM GENRE G";
        return jdbcTemplate.query(sql, (this::genreMapRow));
    }

    @Override
    public List<Rating> findAllRatings() {
        String sql = "SELECT R.* FROM RATING R";
        return jdbcTemplate.query(sql, (this::ratingMapRow));
    }

    @Override
    public Film findById(int number) {
        String sql = "SELECT F.* FROM FILMS F WHERE FILM_ID = ?";
        Film film = jdbcTemplate.query(sql, (this::filmMapRow), new Object[]{number})
                .stream().findAny().orElse(null);
        if (film == null) {
            throw new DataNotFoundException("Фильма с таким id нет в базе");
        }

        return film;
    }

    @Override
    public Genre findGenreById(int genreId) {
        String sql = "SELECT G.* FROM GENRE G WHERE GENRE_ID = ?";
        Genre genre = jdbcTemplate.query(sql, (this::genreMapRow), new Object[]{genreId})
                .stream().findAny().orElse(null);

        if (genre == null) {
            throw new DataNotFoundException("Такого жанра нет в базе данных");
        }

        return genre;
    }

    @Override
    public List<Film> findPopularFilms(int size) {
        String sql = "SELECT COUNT(USER_ID) AS QUANTITY, F.*  " +
                "FROM LIKES " +
                "INNER JOIN FILMS F ON F.FILM_ID = LIKES.FILM_ID " +
                "GROUP BY LIKES.FILM_ID " +
                "ORDER BY QUANTITY DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, this::filmMapRow, size);

    }

    @Override
    public Rating findRatingById(int id) {
        String sql = "SELECT R.* FROM RATING R WHERE RATING_ID = ?";
        Rating rating = jdbcTemplate.query(sql, (this::ratingMapRow), new Object[]{id})
                .stream().findAny().orElse(null);

        if (rating == null) {
            throw new DataNotFoundException("Такого рейтинга нет в базе данных");
        }

        return rating;
    }

    @Override
    public Film updateFilm(Film film) {
        findById(film.getId()); // проверяем на наличие в базе
        String sql = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATE = ?, " +
                "RATING_ID = ? WHERE FILM_ID = ?";
        Object[] params = new Object[]{
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId()
        };

        jdbcTemplate.update(sql, params);

        updateGenres(film);

        film.setGenres(getFilmGenres(film.getId())); // чтобы сортировка была для тестов постман по возрастанию

        return film;
    }

    public Set<Genre> getFilmGenres(int filmId) {
        String sql = "SELECT G.* FROM GENRE G " +
                "INNER JOIN GENRE_FILM GF ON GF.GENRE_ID = G.GENRE_ID " +
                "INNER JOIN FILMS F ON GF.FILM_ID = F.FILM_ID WHERE F.FILM_ID = ? " +
                "GROUP BY G.GENRE_ID " +
                "ORDER BY G.GENRE_ID ASC";
        List<Genre> genres = jdbcTemplate.query(sql, (this::genreMapRow), filmId);

        TreeSet<Genre> genresToReturn = new TreeSet<>(Comparator.comparingInt(Genre::getId));

        genresToReturn.addAll(genres);

        return genresToReturn;
    }

    public Rating getMpa(int filmId) {
        String sql = "SELECT R.* FROM RATING R INNER JOIN FILMS F ON F.RATING_ID = R.RATING_ID WHERE FILM_ID = ?";
        return jdbcTemplate.queryForObject(sql, (this::ratingMapRow), filmId);
    }

    private Set<Integer> getUserLikes(int id) {
        String sql = "SELECT * FROM LIKES WHERE FILM_ID = ?";
        List<Like> likes = jdbcTemplate.query(sql, this::likeMapRow, id);

        Set<Integer> usersIds = new HashSet<>();

        for (Like like : likes) {
            usersIds.add(like.getUserId());
        }
        return usersIds;
    }

    private Film filmMapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("FILM_ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .rate(resultSet.getInt("RATE"))
                .build();
        film.setGenres(getFilmGenres(film.getId()));
        film.setMpa(getMpa(film.getId()));
        film.setLikes(getUserLikes(film.getId()));

        return film;
    }

    private Genre genreMapRow(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("GENRE_ID"))
                .name(rs.getString("NAME"))
                .build();
    }

    private Rating ratingMapRow(ResultSet rs, int rowNum) throws SQLException {
        return Rating.builder()
                .id(rs.getInt("RATING_ID"))
                .name(rs.getString("NAME"))
                .build();
    }

    private Like likeMapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Like.builder()
                .filmId(resultSet.getInt("FILM_ID"))
                .userId(resultSet.getInt("USER_ID"))
                .build();
    }

    private void updateGenres(Film film) {
        if (film.getGenres() != null) {
            String sql1 = "DELETE FROM GENRE_FILM WHERE FILM_ID = ?";
            // очищаем смежную таблицу по ид фильма, удаляя все жанры, относящиеся к нему
            jdbcTemplate.update(sql1, film.getId());
            // и заносим заново
            String sql2 = "INSERT INTO GENRE_FILM(GENRE_ID, FILM_ID) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sql2, genre.getId(), film.getId());
            }
        }
    }
}
