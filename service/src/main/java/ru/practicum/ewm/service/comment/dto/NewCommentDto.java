package ru.practicum.ewm.service.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {
    @NotBlank(message = "Ошибка! Текст комментария не может быть пустым")
    @Size(max = 4096)
    private String text;
}
