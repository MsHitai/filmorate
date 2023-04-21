package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@Data
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

    private int rate;

    @JsonCreator
    public Film(@JsonProperty("id") int id, @JsonProperty("name") @NotBlank String name, @JsonProperty("description")
    String description, @JsonProperty("releaseDate") LocalDate releaseDate, @JsonProperty("duration") int duration,
                @JsonProperty("rate") int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id && duration == film.duration && rate == film.rate && name.equals(film.name) && Objects.equals(description, film.description) && releaseDate.equals(film.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration, rate);
    }
}
