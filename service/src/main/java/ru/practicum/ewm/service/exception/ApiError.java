package ru.practicum.ewm.service.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static ru.practicum.ewm.stats.dto.util.DateTimeFormat.DATETIME_FORMAT;

@Data
@Builder
public class ApiError {

    private HttpStatus status;
    private String reason;
    private String message;
    private StackTraceElement[] errors;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATETIME_FORMAT)
    private LocalDateTime errorTimestamp;
}
