package ru.practicum.model.compilations;

import lombok.*;
import ru.practicum.model.publish.AnswerPublicationDTO;
import ru.practicum.model.publish.Publication;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCompilationsDTO {

    private List<AnswerPublicationDTO> events;
    private Long id;
    private boolean pinned;
    private String title;


    public AnswerCompilationsDTO(Compilations compilations) {
        this.events = toDTO(compilations.getEvents());
        this.id = compilations.getId();
        this.pinned = compilations.getPinned();
        this.title = compilations.getTitle();
    }

    private List<AnswerPublicationDTO> toDTO(List<Publication> publishes) {
        return publishes
                .stream()
                .map(p -> new AnswerPublicationDTO(p))
                .collect(Collectors.toList());
    }
}
