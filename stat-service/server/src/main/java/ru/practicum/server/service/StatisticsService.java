package ru.practicum.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatisticsViewDto;
import ru.practicum.server.exception.BadRequestException;
import ru.practicum.server.mapper.StatisticsViewMapper;
import ru.practicum.server.repository.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.server.mapper.EndpointHitMapper.toEndpointHitDto;
import static ru.practicum.server.mapper.EndpointHitMapper.toEndpointHit;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final StatisticsRepository repository;

    @Transactional(readOnly = true)
    public List<StatisticsViewDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Statistics Service: ОШИБКА: Дата начала должна быть до даты окончания");
        }
        return (unique ? repository.findViewStatsWithUniqueIp(start, end, uris)
                : repository.findViewStats(start, end, uris)).stream()
                .map(StatisticsViewMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EndpointHitDto create(EndpointHitDto dto) {
        return toEndpointHitDto(repository.save(toEndpointHit(dto)));
    }
}
