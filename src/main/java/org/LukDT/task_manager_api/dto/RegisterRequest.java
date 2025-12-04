package org.LukDT.task_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на регистрацию")
public class RegisterRequest {

  @Size(min=2, max=50, message="Имя пользователя может содержать от 2 до 50 символов")
  @Schema(description = "Имя пользователя", example="Dmity")
  @NotBlank(message = "Не может быть пустым")
  private String name;

  @Size(min=10, max=250, message = "Email пользователя должен быть от 10 до 250 символов")
  @Email(message = "Email адрес должен быть в формате user@example.com")
  @Schema(description = "Адрес электронной почты", example = "dmitry@gmail.com")
  @NotBlank(message = "Не может быть пустым")
  private String email;

  @Size(min=8, max=255, message = "Пароль пользователя должен быть от 8 до 255 символов")
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
    message = "Пароль должен содержать минимум 8 символов, цифру, заглавную и строчную букву, специальный символ")
  @Schema(description = "Пароль", example = "superPass147")
  @NotBlank(message = "Не может быть пустым")
  private String password;
}