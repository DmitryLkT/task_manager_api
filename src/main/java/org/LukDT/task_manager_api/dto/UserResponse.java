import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description="Ответ с данными пользователя")
public class UserResponse {

  @Schema(description="ID пользователя", example="1")
  private Long id;

  @Schema(description="Email пользователя", example="dmitry@gmail.com")
  private String email;

  @Schema(description="Имя пользователя", example="Dmitry")
  private String name;

  @Schema(description = "Роли пользователя")
  private Set<RoleResponse> roles;
}