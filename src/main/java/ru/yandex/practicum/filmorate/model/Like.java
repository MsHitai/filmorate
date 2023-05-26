package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Like {

    private int filmId;

    private int userId;

    @JsonCreator
    public Like(@JsonProperty("filmId") int filmId, @JsonProperty("userId") int userId) {
        this.filmId = filmId;
        this.userId = userId;
    }
}
