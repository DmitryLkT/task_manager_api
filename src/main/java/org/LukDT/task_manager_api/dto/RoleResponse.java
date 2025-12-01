import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с информацией о роли")
public class RoleResponse {

  @Schema(description="ID роли", example="1")
  private Long id;

  @Schema(description="Название роли", example="ROLE_USER")
  private String name;
}