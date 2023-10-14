package ru.practicum.ewm.stats.server.service;

import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void hit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
