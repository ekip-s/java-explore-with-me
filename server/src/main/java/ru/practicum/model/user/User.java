package ru.practicum.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.request.Request;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email(message = "email заполнен не правильно.")
    @NotEmpty(message = "email не заполнен")
    @Column(name = "email")
    private String email;
    @NotBlank(message = "Нужно заполнить имя и фамилию.")
    @Column(name = "user_name")
    private String name;
    @Transient
    @OneToMany(mappedBy = "requester")
    private List<Request> requests;
}
