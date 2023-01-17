package ru.practicum.model.comment;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    @NotNull
    private String description;
    @NotNull
    private Long eventId;
}