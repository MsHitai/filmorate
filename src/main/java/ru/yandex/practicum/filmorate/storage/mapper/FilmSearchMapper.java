package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.NonNull;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class FilmSearchMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(@NonNull ResultSet rS, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rS.getInt("mpa_id"));
        mpa.setName(rS.getString("mpa_name"));

        Film film = Film.builder()
                .id(rS.getLong("film_id"))
                .name(rS.getString("name"))
                .description(rS.getString("description"))
                .releaseDate(rS.getDate("release_date").toLocalDate())
                .duration(rS.getInt("duration"))
                .mpa(mpa)
                .rate(rS.getInt("like_count"))
                .build();

        Set<Director> directors = new HashSet<>();
        int directorsQuantity = 0;
        if (rS.getString("director_id") != null) {
            directorsQuantity = rS.getString("director_id").split(";").length;
        }
        for (int i = 0; i < directorsQuantity; i++) {
            Director director = new Director(
                    Integer.parseInt(rS.getString("director_id").split(";")[i]),
                    rS.getString("director_name").split(";")[i]);
            directors.add(director);
        }
        film.setDirectors(directors);

        Set<Genre> genres = new HashSet<>();
        int genresQuantity = 0;
        if (rS.getString("genre_id") != null) {
            genresQuantity = rS.getString("genre_id").split(";").length;
        }
        for (int i = 0; i < genresQuantity; i++) {
            Genre genre = new Genre(
                    Integer.parseInt(rS.getString("genre_id").split(";")[i]),
                    rS.getString("genre_name").split(";")[i]);
            genres.add(genre);
        }
        film.setGenres(genres);


        return film;
    }
}
