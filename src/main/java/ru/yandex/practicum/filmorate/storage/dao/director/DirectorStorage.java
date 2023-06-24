package ru.yandex.practicum.filmorate.storage.dao.director;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.dao.Storage;

public interface DirectorStorage extends Storage<Director> {

    void removeDirector(long id);
}
