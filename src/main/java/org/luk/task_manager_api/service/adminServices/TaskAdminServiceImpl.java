package org.luk.task_manager_api.service.adminServices;

import java.util.List;
import java.util.stream.Collectors;

import org.luk.task_manager_api.dto.TaskResponse;
import org.luk.task_manager_api.exception.customExceptions.ProjectNotFoundException;
import org.luk.task_manager_api.exception.customExceptions.UserNotFoundException;
import org.luk.task_manager_api.repository.ProjectRepository;
import org.luk.task_manager_api.repository.TaskRepository;
import org.luk.task_manager_api.repository.UserRepository;
import org.luk.task_manager_api.service.adminServices.interfaceService.TaskAdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskAdminServiceImpl implements TaskAdminService {
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  private final TaskRepository taskRepository;

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public List<TaskResponse> getAllTasks() {
    return taskRepository.findAll().stream()
            .map(t -> TaskResponse.fromEntity(t))
            .collect(Collectors.toList());
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public List<TaskResponse> getTasksByProject(Long projectId) {
    if(!projectRepository.existsById(projectId)) {
      throw new ProjectNotFoundException(projectId);
    }

    return taskRepository.findByProjectId(projectId).stream()
            .map(t -> TaskResponse.fromEntity(t))
            .collect(Collectors.toList());
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public List<TaskResponse> getTasksByUser(Long userId) {
    if(!!userRepository.existsById(userId)) {
      throw new UserNotFoundException(userId);
    }

    return taskRepository.findByUserId(userId).stream()
            .map(t -> TaskResponse.fromEntity(t))
            .collect(Collectors.toList());
  }
}
