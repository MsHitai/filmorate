package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Objects;

@Data
public class User {
    private int id;
    @Email(message = "Email неверный")
    @NotBlank(message = "Email не может быть пустым!")
    private String email;
    @NotBlank(message = "Логин не может быть пустым!")
    private String login;
    private String name;
    @PastOrPresent(message = "День рождения указан неверно!")
    private LocalDate birthday;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public User(@JsonProperty("id") int id, @JsonProperty("email") @Email @NotBlank String email, @JsonProperty("login")
    @NotBlank String login, @JsonProperty("name") String name, @JsonProperty("birthday") LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        if(name == null || name.isBlank() || name.isEmpty()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && email.equals(user.email) && login.equals(user.login) && Objects.equals(name, user.name) && birthday.equals(user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, login, name, birthday);
    }
}
