package ru.practicum.ewm.stats.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class ViewStatsDto {

    private String app;

    private String uri;

    private Long hits;
}
