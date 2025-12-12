package org.luk.task_manager_api.dto;

import org.luk.task_manager_api.model.Role;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Ответ с информацией о роли")
public class RoleResponse {

  @Schema(description="ID роли", example="1")
  private Long id;

  @Schema(description="Название роли", example="ROLE_USER")
  private String name;

  public static RoleResponse fromEntity(Role role) {
    return RoleResponse.builder()
            .id(role.getId())
            .name(role.getName())
            .build();
  }
}