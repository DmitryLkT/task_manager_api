import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Запрос на аутентификацию")
public class SignInRequest {
  @Size(min=10, max=250, message = "Email пользователя должен быть от 10 до 250 символов")
  @Email(message = "Email адрес должен быть в формате user@example.com")
  @Schema(description = "Адрес электронной почты", example = "dmitry@gmail.com")
  @NotBlank(message = "Не может быть пустым")
  private String email;

  @Size(min=8, max=255, message = "Пароль пользователя должен быть от 8 до 255 символов")
  @Schema(description = "Пароль", example = "superPass147")
  @NotBlank(message = "Не может быть пустым")
  private String password;
}
