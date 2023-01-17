package ru.practicum.model.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.model.publish.Publication;
import ru.practicum.model.reason.Reason;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "publish_id", referencedColumnName = "id")
    private Publication publication;
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;
    @Column(name = "created")
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "canceling_reason_id", referencedColumnName = "id")
    private Reason reason;

    @Enumerated(EnumType.ORDINAL)
    private CommentsStatus status;


    public Comment(CommentDTO comment) {
        this.description = comment.getDescription();
        this.publication = new Publication(comment.getEventId());
        this.created = LocalDateTime.now();
        this.status = CommentsStatus.NEW;
    }
}
