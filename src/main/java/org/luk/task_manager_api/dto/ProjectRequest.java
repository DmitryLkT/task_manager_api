package org.luk.task_manager_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectRequest {

  @Size(min=1, max=75, message = "Название проекта может содержать от 1 до 75 символов")
  @Schema(description = "Название проекта", example = "task_manager_api")
  @NotBlank(message = "Не может быть пустым")
  @Pattern(regexp = "^[a-zA-Z0-9]+[a-zA-Z0-9_-]*[a-zA-Z0-9]+$",
    message = "Разрешены английские буквы, цифры, дефис и подчеркивание в середине")
  private String title;

  @Size(min=1, max=500, message = "Описание проекта может содержать от 1 до 500 символов")
  @Schema(description = "Описание проекта")
  private String description;
}
