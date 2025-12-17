package org.luk.task_manager_api.controller;

import org.luk.task_manager_api.dto.TaskRequest;
import org.luk.task_manager_api.dto.TaskResponse;
import org.luk.task_manager_api.model.Status;
import org.luk.task_manager_api.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
  private final TaskService taskService;

  @PutMapping("/{id}")
  public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, 
                                                  @Valid @RequestBody TaskRequest request) {
    return ResponseEntity.ok(taskService.updateTask(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<TaskResponse> changeStatus(@PathVariable Long id, @RequestParam Status status) {
    return ResponseEntity.ok(taskService.changeStatus(id, status));
  }
}
