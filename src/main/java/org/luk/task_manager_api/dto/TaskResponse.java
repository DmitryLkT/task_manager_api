package org.luk.task_manager_api.dto;

import java.time.LocalDateTime;

import org.luk.task_manager_api.model.Status;
import org.luk.task_manager_api.model.Task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description="Ответ с данными задачи")
public class TaskResponse {
  private Long id;
  private String title;
  private String description;
  private Status status;
  private String projectTitle;
  private String userName;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static TaskResponse fromEntity(Task task) {
    return TaskResponse.builder()
              .id(task.getId())
              .title(task.getTitle())
              .description(task.getDescription())
              .status(task.getStatus())
              .projectTitle(task.getProject().getTitle())
              .userName(task.getUser().getName())
              .createdAt(task.getCreatedAt())
              .updatedAt(task.getUpdatedAt())
              .build();
  }
}
