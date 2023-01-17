package ru.practicum.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsNodeDTO {
    private String app;
    private String uri;
    private Long hits;
}
