package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    @Email(message = "Email неверный")
    @NotBlank(message = "Email не может быть пустым!")
    private String email;
    @NotBlank(message = "Логин не может быть пустым!")
    @Pattern(regexp = "\\S*", message = "Логин не может содержать пробелы.")
    private String login;
    private String name;
    @PastOrPresent(message = "День рождения указан неверно!")
    private LocalDate birthday;

    @JsonIgnore
    private Set<Friend> friends = new LinkedHashSet<>();

    @JsonCreator()
    public User(@JsonProperty("id") int id, @JsonProperty("email") @Email @NotBlank String email, @JsonProperty("login")
    @NotBlank String login, @JsonProperty("name") String name, @JsonProperty("birthday") LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name == null || name.isBlank() || name.isEmpty()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }

    public User(int id, String email, String login, String name, LocalDate birthday, Set<Friend> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = friends;
    }
}
