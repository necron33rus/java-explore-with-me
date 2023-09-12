package ru.practicum.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsClient extends BaseClient {

    public StatisticsClient(@Value("${stats-service.url}") String url, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStatistic(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("uris", uris);
        params.put("unique", unique);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", params);
    }

    public ResponseEntity<Object> createHit(EndpointHitDto dto) {
        return post("/hit", dto);
    }
}
