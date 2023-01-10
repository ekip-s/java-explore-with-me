package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.dto.PublishDTO;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publish {

    private long id;
    private String annotation;
    private Category category;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String title;

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
