package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class Friends {
    @NotNull
    private long userId;
    @NotNull
    private long friendId;
    @NotNull
    private boolean isMutual;
}
