package org.luk.task_manager_api.service.adminServices;

import java.util.List;
import java.util.stream.Collectors;

import org.luk.task_manager_api.dto.TaskResponse;
import org.luk.task_manager_api.repository.TaskRepository;
import org.luk.task_manager_api.service.CurrentUserService;
import org.luk.task_manager_api.service.adminServices.interfaceService.TaskAdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskAdminServiceImpl implements TaskAdminService {
  private final CurrentUserService currentUserService;
  private final TaskRepository taskRepository;

  @Override
  @Transactional
  public List<TaskResponse> getAllTasks() {
    checkAdminAccess();

    return taskRepository.findAll().stream()
            .map(t -> TaskResponse.fromEntity(t))
            .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public List<TaskResponse> getTasksByProject(Long projectId) {
    checkAdminAccess();

    return taskRepository.findByProjectId(projectId).stream()
            .map(t -> TaskResponse.fromEntity(t))
            .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public List<TaskResponse> getTasksByUser(Long userId) {
    checkAdminAccess();

    return taskRepository.findByUserId(userId).stream()
            .map(t -> TaskResponse.fromEntity(t))
            .collect(Collectors.toList());
  }

  private void checkAdminAccess() {
        if (!currentUserService.isAdmin()) {
      throw new RuntimeException("Admin role required");
    }
  }
}
