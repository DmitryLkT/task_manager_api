package org.luk.task_manager_api.controller;

import java.util.List;

import org.luk.task_manager_api.dto.ProjectRequest;
import org.luk.task_manager_api.dto.ProjectResponse;
import org.luk.task_manager_api.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {
  private final ProjectService projectService;
  
  @GetMapping
  public ResponseEntity<List<ProjectResponse>> getAllProjectsFromUser() {
    return ResponseEntity.ok(projectService.getAllProjectsFromUser());
  }

  @GetMapping("/{title}")
  public ResponseEntity<ProjectResponse> getProject(@PathVariable String title) {
    ProjectResponse response = projectService.getProject(title);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<ProjectResponse> createdProject(@Valid @RequestBody ProjectRequest request) {
    ProjectResponse response = projectService.createdProject(request);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{title}")
  public ResponseEntity<ProjectResponse> updateProject(@PathVariable String title,
                                                      @Valid @RequestBody ProjectRequest request) {
    ProjectResponse response = projectService.updateProject(title, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{title}")
  public ResponseEntity<Void> deleteProject(@PathVariable String title) {
    projectService.deleteProject(title);
    return ResponseEntity.noContent().build();
  }
}
