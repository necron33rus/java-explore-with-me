package ru.practicum.ewm.service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.service.constant.DateTimeFormat.DATETIME_FORMAT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank(message = "Ошибка! Аннотация не может быть пустой")
    @Size(min = 20, max = 2000, message = "Ошибка! Длина аннотации должна быть от 20 до 2000 символов")
    private String annotation;

    @NotNull(message = "Ошибка! Категория не должна быть null")
    private Long category;

    @NotBlank(message = "Ошибка! Описание не может быть пустым")
    @Size(min = 20, max = 7000, message = "Ошибка! Длина описания должна быть от 20 до 7000 символов")
    private String description;

    @NotNull(message = "Ошибка! Дата события не должна быть null")
    @Future(message = "Ошибка! Дата события должны быть в будущем")
    @JsonProperty("eventDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private LocalDateTime eventTimestamp;

    @NotNull(message = "Ошибка! Локация не должка быть null")
    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank(message = "Ошибка! Название не может быть пустым")
    @Size(min = 3, max = 120, message = "Ошибка! Длина заголовка должны быть от 3 до 120 символов")
    private String title;
}
