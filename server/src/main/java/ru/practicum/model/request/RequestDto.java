package ru.practicum.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private RequestStatus status;


    public RequestDto(Request request) {
        this.created = request.getCreated();
        this.event = request.getEvent().getId();
        this.id = request.getId();
        this.requester = request.getRequester().getId();
        this.status = request.getStatus();
    }
}
