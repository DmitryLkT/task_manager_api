package org.luk.task_manager_api.service.adminServices;

import java.util.List;
import java.util.stream.Collectors;

import org.luk.task_manager_api.dto.ProjectResponse;
import org.luk.task_manager_api.exception.customExceptions.ProjectNotFoundException;
import org.luk.task_manager_api.exception.customExceptions.UserNotFoundException;
import org.luk.task_manager_api.model.Project;
import org.luk.task_manager_api.model.User;
import org.luk.task_manager_api.repository.ProjectRepository;
import org.luk.task_manager_api.repository.UserRepository;
import org.luk.task_manager_api.service.adminServices.interfaceService.ProjectAdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectAdminServiceImpl implements ProjectAdminService {
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  
  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public List<ProjectResponse> getAllProjects() {
    return projectRepository.findAll().stream()
              .map(p -> ProjectResponse.fromEntity(p))
              .collect(Collectors.toList());
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public ProjectResponse getProjectById(Long projectId) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new ProjectNotFoundException(projectId));
    
    return ProjectResponse.fromEntity(project);    
  }

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public List<ProjectResponse> getProjectsByUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    return projectRepository.findByOwner(user).stream()
              .map(p -> ProjectResponse.fromEntity(p))
              .collect(Collectors.toList());    
  }
}
