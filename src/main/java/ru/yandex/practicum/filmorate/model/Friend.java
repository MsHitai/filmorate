package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friend {

    private int friendId;

    @JsonCreator
    public Friend(@JsonProperty("id") int friendId) {
        this.friendId = friendId;
    }
}
