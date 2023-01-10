package ru.practicum.client.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

@Service
public class RequestsClient extends BaseClient {

    private static final String API_PREFIX = "/users";


    @Autowired
    public RequestsClient(@Value("${explore-with-me-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getRequests(long userId) {
        return get("/" + userId + "/requests");
    }

    public ResponseEntity<Object> createRequests(long userId, long eventId) {
        return post("/" + userId + "/requests?eventId=" + eventId);
    }

    public ResponseEntity<Object> cancelRequests(long userId, long requestId) {
        return patch("/" + userId + "/requests/" + requestId + "/cancel");
    }
}
