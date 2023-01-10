package ru.practicum.client.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;
import ru.practicum.model.Category;

@Service
public class CategoriesAdminClient extends BaseClient {

    private static final String API_PREFIX = "/admin/categories";


    @Autowired
    public CategoriesAdminClient(@Value("${explore-with-me-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createCategories(Category category) {
        return post("", category);
    }

    public ResponseEntity<Object> patchCategories(Category category) {
        return patch("", category);
    }

    public ResponseEntity<Object> deleteCategories(Long catId) {
        return delete("/" + catId);
    }
}
