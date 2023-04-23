package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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
}
