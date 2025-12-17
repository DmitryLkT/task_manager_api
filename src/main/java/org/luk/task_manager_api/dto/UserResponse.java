package org.luk.task_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import org.luk.task_manager_api.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description="Ответ с данными пользователя")
public class UserResponse {

  @Schema(description="ID пользователя", example="1")
  private Long id;

  @Schema(description="Имя пользователя", example="Dmitry")
  private String name;

  @Schema(description="Email пользователя", example="dmitry@gmail.com")
  private String email;

  @Schema(description = "Роли пользователя")
  private String role;

  public static UserResponse fromEntity(User user) {
    return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .role(user.getRole().getName())
            .build();
  }
}