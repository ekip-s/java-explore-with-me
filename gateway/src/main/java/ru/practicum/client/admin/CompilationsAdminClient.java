package ru.practicum.client.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.model.Compilations;

@Service
public class CompilationsAdminClient extends BaseClient {

    private static final String API_PREFIX = "/admin/compilations";

    @Autowired
    public CompilationsAdminClient(@Value("${explore-with-me-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createCompilations(Compilations compilations) {
        return post("", compilations);
    }

    public ResponseEntity<Object> deleteCompilations(long compId) {
        return delete("/" + compId);
    }

    public ResponseEntity<Object> deleteEvent(long compId, long eventId) {
        return delete("/" + compId + "/events/" + eventId);
    }

    public ResponseEntity<Object> addEvent(long compId, long eventId) {
        return patch("/" + compId + "/events/" + eventId);
    }

    public ResponseEntity<Object> unpinCompilations(long compId) {
        return delete("/" + compId + "/pin");
    }

    public ResponseEntity<Object> toFixCompilations(long compId) {
        return patch("/" + compId + "/pin");
    }
}
