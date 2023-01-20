package ru.practicum.model.compilations;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationsDTO {

    private Long id;
    private boolean pinned;
    @NotEmpty(message = "Заголовок не заполнен.")
    private String title;
    private List<Long> events;
}
