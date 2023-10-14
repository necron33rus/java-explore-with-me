package ru.practicum.ewm.stats.server.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.dto.EndpointHitDto;
import ru.practicum.ewm.stats.dto.ViewStatsDto;
import ru.practicum.ewm.stats.server.exception.BadRequestException;
import ru.practicum.ewm.stats.server.mapper.StatsMapper;
import ru.practicum.ewm.stats.server.model.ViewStatsProjection;
import ru.practicum.ewm.stats.server.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final EndpointHitRepository endpointHitRepository;

    @Transactional
    @Override
    public void hit(EndpointHitDto endpointHitDto) {
        endpointHitRepository.save(StatsMapper.INSTANCE.fromDto(endpointHitDto));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new BadRequestException("Start/End date is not present, " +
                    "or start date is before end date. start=" + start + ", end=" + end);
        }
        List<ViewStatsProjection> stats = BooleanUtils.isTrue(unique) ? endpointHitRepository.findUniqueStats(start, end, uris) : endpointHitRepository.findNotUniqueStats(start, end, uris);

        return stats.stream()
                .map(result -> ViewStatsDto.builder()
                        .app(result.getApp())
                        .uri(result.getUri())
                        .hits(result.getHits())
                        .build())
                .collect(Collectors.toList());
    }
}
