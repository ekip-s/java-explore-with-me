package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicGetPublish {

    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private SortOptions sort;
    private int from;
    private int size;

    public PublicGetPublish(String text, List<Long> categories, boolean paid,
                            LocalDateTime rangeStart, LocalDateTime rangeEnd,
                            Boolean onlyAvailable, String sort, int from, int size) {
        if(sort.equals("EVENT_DATE")) {
            this.sort = SortOptions.EVENT_DATE;
        } else if (sort.equals("VIEWS")) {
            this.sort = SortOptions.VIEWS;
        }
        this.text = text;
        this.categories = categories;
        this.paid = paid;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.onlyAvailable = onlyAvailable;
        this.from = from;
        this.size = size;
    }
}
