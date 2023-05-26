package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.yandex.practicum.filmorate.validators.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
public class Film {

    private int id;
    @NotBlank(message = "Название не может быть пустым!")
    private String name;
    @Size(max = 200, message = "Максимум символов - 200")
    private String description;
    @ReleaseDate(message = "Дата релиза неверна!")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность не может быть отрицательной!")
    private int duration;

    @JsonIgnore
    private Set<Integer> likes = new HashSet<>();

    private int rate;

    private Rating mpa;

    private Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));

    @JsonCreator
    public Film(@JsonProperty("id") int id, @JsonProperty("name") @NotBlank String name, @JsonProperty("description")
    String description, @JsonProperty("releaseDate") LocalDate releaseDate, @JsonProperty("duration") int duration,
                @JsonProperty("rate") int rate, @JsonProperty("mpa") Rating mpa,
                @JsonProperty("genres") Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, Set<Integer> likes,
                int rate, Rating mpa, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
    }
}
