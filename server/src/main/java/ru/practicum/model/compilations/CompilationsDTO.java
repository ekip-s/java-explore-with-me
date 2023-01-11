package ru.practicum.model.compilations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationsDTO {

    private long id;
    private boolean pinned;
    @NotEmpty(message = "Заголовок не заполнен.")
    private String title;
    private List<Long> events;
}
