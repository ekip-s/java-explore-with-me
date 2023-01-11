package ru.practicum.model.compilations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.publish.AnswerPublishDTO;
import ru.practicum.model.publish.Publish;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCompilationsDTO {

    private List<AnswerPublishDTO> events;
    private long id;
    private boolean pinned;
    private String title;


    public AnswerCompilationsDTO(Compilations compilations) {
        this.events = toDTO(compilations.getEvents());
        this.id = compilations.getId();
        this.pinned = compilations.getPinned();
        this.title = compilations.getTitle();
    }

    private List<AnswerPublishDTO> toDTO(List<Publish> publishes) {
        return publishes
                .stream()
                .map(p -> new AnswerPublishDTO(p))
                .collect(Collectors.toList());
    }
}
