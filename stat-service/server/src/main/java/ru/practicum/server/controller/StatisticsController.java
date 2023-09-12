package ru.practicum.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatisticsViewDto;
import ru.practicum.server.service.StatisticsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class StatisticsController {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final StatisticsService service;

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatisticsViewDto> get(@RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime start,
                                  @RequestParam @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Statistics Controller: GET ../stats Запрошена статистика за промежуток: {}-{} и уникальностью {}", start, end, unique);
        return service.get(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto create(@RequestBody EndpointHitDto dto) {
        log.info("Statistics Controller: POST ../hit Добавлена статистика");
        return service.create(dto);
    }
}
