package ru.practicum.model.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    @NotNull
    private String description;
    @NotNull
    private Long event_id;
}
