package ru.practicum.model.publish;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.model.Location;
import ru.practicum.model.category.Category;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerPublicationDTO {
    private String annotation;
    private Category category;
    private int confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    private User initiator;
    private Location location;
    private boolean paid;
    private int participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private PublicationState state;
    private String title;
    private long views;


    public AnswerPublicationDTO(Publication publish) {
        this.annotation = publish.getAnnotation();
        this.category = publish.getCategory();
        this.confirmedRequests = publish.getConfirmedRequests();
        this.createdOn = publish.getCreatedOn();
        this.description = publish.getDescription();
        this.eventDate = publish.getEventDate();
        this.id = publish.getId();
        this.initiator = publish.getInitiator();
        this.location = publish.getLocation();
        this.paid = publish.getPaid();
        this.participantLimit = publish.getParticipantLimit();
        this.publishedOn = publish.getPublishedOn();
        this.requestModeration = publish.getRequestModeration();
        this.state = publish.getState();
        this.title = publish.getTitle();
        this.views = publish.getViews();
    }
}
