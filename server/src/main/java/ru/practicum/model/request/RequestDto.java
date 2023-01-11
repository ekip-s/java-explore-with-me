package ru.practicum.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private long  event;
    private long id;
    private long requester;
    private RequestStatus status;


    public RequestDto(Request request) {
        this.created = request.getCreated();
        this.event = request.getEvent().getId();
        this.id = request.getId();
        this.requester = request.getRequester().getId();
        this.status = request.getStatus();
    }
}
