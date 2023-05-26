DELETE FROM FRIENDS;
DELETE FROM LIKES;

DELETE FROM FILMS;
DELETE FROM USERS;

DELETE FROM RATING;
DELETE FROM GENRE;

INSERT INTO GENRE (GENRE_ID, NAME) VALUES (1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'), (4, 'Триллер'),
(5, 'Документальный'), (6, 'Боевик');

INSERT INTO RATING (RATING_ID, NAME) VALUES (1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');