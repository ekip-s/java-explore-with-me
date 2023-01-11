package ru.practicum.model.publish;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.model.compilations.Compilations;
import ru.practicum.model.Location;
import ru.practicum.model.request.Request;
import ru.practicum.model.category.Category;
import ru.practicum.model.user.User;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "publish")
public class Publish {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "annotation")
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    @Column(name = "confirmed_requests")
    private int confirmedRequests;
    @Column(name = "created_on")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime createdOn;
    @Column(name = "description")
    private String description;
    @Column(name = "event_date")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.ORDINAL)
    private PublishState state;
    @Column(name = "title")
    private String title;
    @Column(name = "views")
    private int views;
    @Transient
    @OneToMany(mappedBy = "event")
    private List<Request> requests;
    @Transient
    @ManyToMany(mappedBy = "events")
    private List<Compilations> compilations;

    public Publish(PublishDTO publishDTO) {
        this.id = publishDTO.getId();
        this.annotation = publishDTO.getAnnotation();
        this.category = new Category(publishDTO.getCategory());
        this.description = publishDTO.getDescription();
        this.eventDate = publishDTO.getEventDate();
        this.location = publishDTO.getLocation();
        this.paid = publishDTO.isPaid();
        this.participantLimit = publishDTO.getParticipantLimit();
        this.requestModeration = publishDTO.isRequestModeration();
        this.title = publishDTO.getTitle();
    }

    public Publish(long id) {
        this.id = id;
    }
}
