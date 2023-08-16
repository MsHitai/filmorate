# java-filmorate
Схема базы данных:
![filmogram (1)](https://github.com/MsHitai/java-filmorate/assets/115479428/23337725-9179-43f2-abf1-25bf88c7cac9)

<details>
  <summary>Получить информацию о фильме по id = 1 с названием его рейтинга</summary>

```sql
    SELECT F.*,
           R.NAME
    FROM FILMS F
    INNER JOIN RATING R ON F.RATING_ID = R.RATING_ID
    WHERE FILM_ID = 1;
```

</details>  

<details>
  <summary>Получить информацию о статусе дружбы пользователей</summary>

```sql
    SELECT U.*,
           F.STATUS
    FROM USERS U
    INNER JOIN FRIENDS F ON U.USER_ID = F.USER_ID
    WHERE F.FRIEND_ID = 1;
```

</details>  

Возможности:
- REST controllers - можно ставить лайки фильмам, сортировать фильмы по рейтингу, жанру, заводить друзей, смотреть их любимые фильмы.
- Во время группового проекта были добавлены такие возможности, как поиск, отзывы, рекомендации, ленту событий. Ссылка на групповой проект: https://github.com/MsHitai/java-filmorate-1
