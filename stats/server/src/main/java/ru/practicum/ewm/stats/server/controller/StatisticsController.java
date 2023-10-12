package ru.practicum.ewm.stats.server.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.stats.dto.util.DateTimeFormat.DATETIME_FORMAT;

@RestController
@AllArgsConstructor
@Slf4j
public class StatisticsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody EndpointHitDto endpointHitDto) {
        statsService.hit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = DATETIME_FORMAT) LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = DATETIME_FORMAT) LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris,
                                       @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Поступил запрос на получение статистики: start={}, end={}, uris={}, unique={}",
                start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }
}
