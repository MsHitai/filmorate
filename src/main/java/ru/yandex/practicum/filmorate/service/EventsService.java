package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.events.EventsDao;

import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class EventsService {
    public static final String LIKE_TYPE = "LIKE";
    public static final String REVIEW_TYPE = "REVIEW";
    public static final String FRIEND_TYPE = "FRIEND";
    public static final String ADD_OPERATION = "ADD";
    public static final String REMOVE_OPERATION = "REMOVE";
    public static final String UPDATE_OPERATION = "UPDATE";

    private final EventsDao eventsDao;

    public void addEvent(long userId, long entityId, String eventType, String operation) {
        eventsDao.addEvent(findTimestamp(), userId, eventType, operation, entityId);
    }

    private Long findTimestamp() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
