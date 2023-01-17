package ru.practicum.model.compilations;

import lombok.*;
import ru.practicum.model.publish.Publication;


import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilations {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pinned")
    private Boolean pinned;
    @Column(name = "title")
    private String title;

    @ManyToMany
    @JoinTable(
            name = "publish_compilations",
            joinColumns = @JoinColumn(name = "compilations_id"),
            inverseJoinColumns = @JoinColumn(name = "publish_id")
    )
    private List<Publication> events;

    public Compilations(CompilationsDTO compilationsDTO, List<Publication> events) {
        this.id = compilationsDTO.getId();
        this.pinned = compilationsDTO.isPinned();
        this.title = compilationsDTO.getTitle();
        this.events = events;
    }


}
