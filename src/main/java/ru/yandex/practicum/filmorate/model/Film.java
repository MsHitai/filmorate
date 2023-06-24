package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.validation.interfaces.ValidDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Validated
public class Film {
    private long id;
    @NotBlank(message = "Название не должно быть пустым")
    private String name;
    @NotNull
    @Size(max = 200, message = "Максимальная длина описания 200 символов")
    private String description;
    @ValidDate
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;
    @NotNull(message = "Должен быть указан рейтинг MPA")
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    private int rate;
    private Set<Director> directors = new HashSet<>();

    @Builder
    public Film(long id,
                String name,
                String description,
                LocalDate releaseDate,
                int duration, Mpa mpa,
                int rate) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = rate;
    }

}
