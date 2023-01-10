package ru.practicum.client.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.model.Publish;

import java.util.Map;

@Service
public class PublishUserClientPrivate extends BaseClient {

    private static final String API_PREFIX = "/users";


    @Autowired
    public PublishUserClientPrivate(@Value("${explore-with-me-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getUserEventsPrivate(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/" + userId + "/events?from={from}&size={size}", parameters);
    }

    public ResponseEntity<Object> updateUserEventsPrivate(long userId, Publish publish, long eventId) {
        return patch("/" + userId + "/events?eventId=" + eventId, publish);
    }

    public ResponseEntity<Object> addUserEventsPrivate(long userId, Publish publish) {
        return post("/" + userId + "/events", publish);
    }

    public ResponseEntity<Object> getUserEventsByIdPrivate(long userId, long eventId) {
        return get("/" + userId + "/events/" + eventId);
    }

    public ResponseEntity<Object> cancellationUserEventsPrivate(long userId, long eventId) {
        return patch("/" + userId + "/events/" + eventId);
    }

    public ResponseEntity<Object> participationRequestsPrivate(long userId, long eventId) {
        return get("/" + userId + "/events/" + eventId + "/requests");
    }

    public ResponseEntity<Object> approvalApplicationPrivate(long userId, long eventId, long reqId) {
        return patch("/" + userId + "/events/" + eventId + "/requests/" + reqId + "/confirm");
    }

    public ResponseEntity<Object> cancellationApplicationPrivate(long userId, long eventId, long reqId) {
        return patch("/" + userId + "/events/" + eventId + "/requests/" + reqId + "/reject");
    }
}
