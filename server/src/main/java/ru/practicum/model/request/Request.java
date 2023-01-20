package ru.practicum.model.request;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.model.publish.Publication;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
public class Request {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    private Publication event;
    @ManyToOne
    @JoinColumn(name = "requester_id", referencedColumnName = "id")
    private User requester;
    @Enumerated(EnumType.ORDINAL)
    private RequestStatus status;

    public Request(Publication event, User requester) {
        this.created = LocalDateTime.now();
        this.event = event;
        this.requester = requester;
        this.status = RequestStatus.PENDING;
    }
}
