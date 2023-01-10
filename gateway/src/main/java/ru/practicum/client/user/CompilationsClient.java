package ru.practicum.client.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class CompilationsClient extends BaseClient {

    private static final String API_PREFIX = "/compilations";

    @Autowired
    public CompilationsClient(@Value("${explore-with-me-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getCompilations(Boolean pinned, int from, int size) {
        if(pinned == null) {
            Map<String, Object> parameters = Map.of(
                    "from", from,
                    "size", size
            );
            return get("?from={from}&size={size}", parameters);
        } else {
            Map<String, Object> parameters = Map.of(
                    "pinned", pinned,
                    "from", from,
                    "size", size
            );
            return get("?pinned={pinned}&from={from}&size={size}", parameters);
        }
    }

    public ResponseEntity<Object> getCompilationsById(long compId) {
        return get("/" + compId);
    }
}
