package ru.practicum.client.admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.model.GetPublish;
import ru.practicum.model.Publish;

@Service
public class PublishAdminClient extends BaseClient {

    private static final String API_PREFIX = "/admin/events";


    @Autowired
    public PublishAdminClient(@Value("${explore-with-me-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getPublishAdmin(GetPublish getPublish) {
        return post("/body", getPublish);
    }

    public ResponseEntity<Object> putPublishAdmin(Long eventId, Publish publish) {
        return put("/" + eventId, publish);
    }

    public ResponseEntity<Object> approvePublishAdmin(Long eventId) {
        return patch("/" + eventId + "/publish");
    }

    public ResponseEntity<Object> refusePublishAdmin(Long eventId) {
        return patch("/" + eventId + "/reject");
    }
}
