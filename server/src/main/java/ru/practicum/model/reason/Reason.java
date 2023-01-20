package ru.practicum.model.reason;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "canceling_or_delete_reason")
public class Reason {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "reason_name")
    private String name;

    public Reason(String name) {
        this.name = name;
    }
}
