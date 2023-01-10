package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationsDTO {
    private long id;
    private boolean pinned;

    private String title;
    private Long[] events;
}
