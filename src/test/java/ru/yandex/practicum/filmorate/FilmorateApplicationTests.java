package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

	private final UserService userService;

	private final FilmService filmService;

	private Film film;
	private User user;

	@BeforeEach
	public void setUp() {
		film = Film.builder()
				.id(1)
				.name("SomeTestFilm")
				.description("SomeTestDescription")
				.releaseDate(LocalDate.of(2000, 1, 11))
				.duration(110)
				.mpa(new Mpa(1, "G"))
				.build();

		user = User.builder()
				.id(1)
				.name("SomeUser")
				.login("SomeLogin")
				.email("login@mail.ru")
				.birthday(LocalDate.of(1990, Month.DECEMBER, 5))
				.build();
	}

	@Test
	public void testCommonPopularFilms() {
		User user1 = User.builder()
				.id(2)
				.name("Johnny1")
				.email("johnnymail1@mail.ru")
				.login("mrJohnny1")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		User user2 = User.builder()
				.id(3)
				.name("Johnny2")
				.email("johnnymail2@mail.ru")
				.login("mrJohnny2")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		userService.create(user);
		userService.create(user1);
		userService.create(user2);

		Film film1 = Film.builder()
				.id(2)
				.name("SomeTestFilm2")
				.description("SomeTestDescription2")
				.releaseDate(LocalDate.of(2000, 1, 11))
				.duration(110)
				.mpa(new Mpa(1, "G"))
				.build();

		filmService.create(film);
		filmService.create(film1);

		filmService.addNewLike(film.getId(), user.getId());
		filmService.addNewLike(film.getId(), user1.getId());
		filmService.addNewLike(film.getId(), user2.getId());

		filmService.addNewLike(film1.getId(), user.getId());
		filmService.addNewLike(film1.getId(), user1.getId());

		List<Film> films = filmService.getCommonPopularFilms(user.getId(), user1.getId());

		assertThat(films.size(), is(2));
		assertThat(films.get(0), is(film));
	}

	@Test
	void contextLoads() {
	}

}
