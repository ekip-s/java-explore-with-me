package ru.practicum.model.compilations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.publish.Publish;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilations {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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
    private List<Publish> events;

    public Compilations(CompilationsDTO compilationsDTO, List<Publish> events) {
        this.id = compilationsDTO.getId();
        this.pinned = compilationsDTO.isPinned();
        this.title = compilationsDTO.getTitle();
        this.events = events;
    }


}
