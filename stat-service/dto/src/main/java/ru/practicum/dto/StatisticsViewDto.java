package ru.practicum.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class StatisticsViewDto {

    private String app;

    private String uri;

    private Long hits;
}
