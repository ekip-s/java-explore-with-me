package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.CompilationsDTO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compilations {

    private long id;
    private boolean pinned;

    private String title;
    private List<Publish> events;

    public Compilations(CompilationsDTO compilationsDTO) {
        this.id = compilationsDTO.getId();
        this.pinned = compilationsDTO.isPinned();
        this.title = compilationsDTO.getTitle();
        this.events = createEventsList(compilationsDTO.getEvents());
    }

    private List<Publish> createEventsList(Long[] idList) {
        List<Long> list = Arrays.asList(idList);
        return list.stream()
                .map(i -> new Publish(i))
                .collect(Collectors.toList());
    }
}
