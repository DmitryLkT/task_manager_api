package org.luk.task_manager_api.dto;

import java.time.LocalDateTime;

import org.luk.task_manager_api.model.Project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Ответ с данными проекта")
public class ProjectResponse {
  private Long id;
  private String title;
  private String description;
  private LocalDateTime createdAt;
  private String ownerName;

  public static ProjectResponse fromEntity(Project project) {
    return ProjectResponse.builder()
              .id(project.getId())
              .title(project.getTitle())
              .description(project.getDescription())
              .createdAt(project.getCreatedAt())
              .ownerName(project.getOwner().getName())
              .build();
  }
}
