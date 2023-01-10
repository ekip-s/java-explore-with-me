package ru.practicum.client.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.model.PublicGetPublish;

@Service
public class PublishUserClientPublic extends BaseClient {

    private static final String API_PREFIX = "/events";


    @Autowired
    public PublishUserClientPublic(@Value("${explore-with-me-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getEventsPublic(PublicGetPublish publicGetPublish) {
        return post("/body", publicGetPublish);
    }

    public ResponseEntity<Object> getEventsByIdPublic (long id) {
        return get("/" + id);
    }

}
