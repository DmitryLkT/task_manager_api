package org.luk.task_manager_api.controller;

import java.util.List;

import org.luk.task_manager_api.dto.ProjectResponse;
import org.luk.task_manager_api.dto.TaskResponse;
import org.luk.task_manager_api.dto.UserResponse;
import org.luk.task_manager_api.service.adminServices.interfaceService.ProjectAdminService;
import org.luk.task_manager_api.service.adminServices.interfaceService.TaskAdminService;
import org.luk.task_manager_api.service.adminServices.interfaceService.UserAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
  private final UserAdminService userAdminService;
  private final ProjectAdminService projectAdminService;
  private final TaskAdminService taskAdminService;

  @GetMapping("/users")
  public ResponseEntity<List<UserResponse>> getAllUser() {
    return ResponseEntity.ok(userAdminService.getAllUser());
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userAdminService.getUserById(id));
  }

  @PatchMapping("/users/{id}")
  public ResponseEntity<Void> updateUserRole(@PathVariable Long id, @RequestBody String role) {
    userAdminService.updateUserRole(id, role);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/projects")
  public ResponseEntity<List<ProjectResponse>> getAllProjects() {
    return ResponseEntity.ok(projectAdminService.getAllProjects());
  }

  @GetMapping("/projects/{id}")
  public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
    return ResponseEntity.ok(projectAdminService.getProjectById(id));
  }

  @GetMapping("/projects/user/{id}")
  public ResponseEntity<List<ProjectResponse>> getProjectsByUser(@PathVariable Long id) {
    return ResponseEntity.ok(projectAdminService.getProjectsByUser(id));
  }

  @GetMapping("/tasks")
  public ResponseEntity<List<TaskResponse>> getAllTasks() {
    return ResponseEntity.ok(taskAdminService.getAllTasks());
  }

  @GetMapping("/tasks/project/{id}")
  public ResponseEntity<List<TaskResponse>> getTasksByProject(@PathVariable Long id) {
    return ResponseEntity.ok(taskAdminService.getTasksByProject(id));
  }

  @GetMapping("/task/user/{id}")
  public ResponseEntity<List<TaskResponse>> getTasksByUser(@PathVariable Long id) {
    return ResponseEntity.ok(taskAdminService.getTasksByUser(id));
  }
}
