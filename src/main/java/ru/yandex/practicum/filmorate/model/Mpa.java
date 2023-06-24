package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    @NotNull
    private int id;
    @NotNull
    private String name;
}
