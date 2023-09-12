package ru.practicum.server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.StatisticsViewDto;
import ru.practicum.server.model.StatisticsView;

@UtilityClass
public class StatisticsViewMapper {
    public static StatisticsViewDto toViewStatsDto(StatisticsView statisticsView) {
        return StatisticsViewDto.builder()
                .app(statisticsView.getApp())
                .uri(statisticsView.getUri())
                .hits(statisticsView.getHits())
                .build();
    }
}
