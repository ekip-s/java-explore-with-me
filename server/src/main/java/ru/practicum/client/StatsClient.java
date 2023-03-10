package ru.practicum.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.model.StatsDto;
import ru.practicum.model.StatsNode;
import ru.practicum.model.publish.Publication;

import java.time.format.DateTimeFormatter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

@Component
public class StatsClient extends BaseClient {

    private static final String API_PREFIX_POST = "/hit";

    private final ObjectMapper objectMapper;

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String url, ObjectMapper objectMapper) {
        super(url);
        this.objectMapper = objectMapper;
    }


    public void addStatsNode(String uri, String ip) {
        post(API_PREFIX_POST, new StatsNode(API_PREFIX_POST, uri, ip));
    }

    public List<StatsDto> getStats(List<Publication> events, boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String[] uris = events.stream().map(this::createUri).toArray(String[]::new);
        LocalDateTime start = events.stream().map(Publication::getCreatedOn).min(LocalDateTime::compareTo).orElseThrow();
        parameters.put("start", URLEncoder.encode(format.format(start), StandardCharsets.UTF_8));
        parameters.put("end", URLEncoder.encode(format.format(LocalDateTime.now()), StandardCharsets.UTF_8));
        parameters.put("unique", unique);
        parameters.put("uris", uris);
        try {
            return Arrays.asList(objectMapper.readValue(
                    get("/stats?start={start}&end={end}&unique={unique}&uris={uris}", parameters).getBody(),
                    StatsDto[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String createUri(Publication publish) {
        return "/events/" + publish.getId();
    }
}