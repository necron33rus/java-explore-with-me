package ru.practicum.ewm.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    @NotBlank(message = "Ошибка! E-mail не может быть пустым")
    @Size(max = 254, min = 6, message = "Ошибка! E-mail должен быть длиной от 6 до 254 символов")
    @Email(message = "Ошибка! Некорректный формат")
    private String email;

    @NotBlank(message = "Ошибка! Имя не может быть пустым")
    @Size(max = 250, min = 2, message = "Ошибка! Имя должно быть длиной от 2 до 250 символов")
    private String name;
}
