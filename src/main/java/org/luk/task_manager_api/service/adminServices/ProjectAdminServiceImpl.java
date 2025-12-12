package org.luk.task_manager_api.service.adminServices;

import java.util.List;
import java.util.stream.Collectors;

import org.luk.task_manager_api.dto.ProjectResponse;
import org.luk.task_manager_api.model.Project;
import org.luk.task_manager_api.model.User;
import org.luk.task_manager_api.repository.ProjectRepository;
import org.luk.task_manager_api.repository.UserRepository;
import org.luk.task_manager_api.service.CurrentUserService;
import org.luk.task_manager_api.service.adminServices.interfaceService.ProjectAdminService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectAdminServiceImpl implements ProjectAdminService {
  private final CurrentUserService currentUserService;
  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  
  @Override
  @Transactional
  public List<ProjectResponse> getAllProjects() {
    checkAdminAccess();

    return projectRepository.findAll().stream()
              .map(p -> ProjectResponse.fromEntity(p))
              .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ProjectResponse getProjectById(Long projectId) {
    checkAdminAccess();

    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
    
    return ProjectResponse.fromEntity(project);    
  }

  @Override
  @Transactional
  public List<ProjectResponse> getProjectsByUser(Long userId) {
    checkAdminAccess();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

    return projectRepository.findByOwner(user).stream()
              .map(p -> ProjectResponse.fromEntity(p))
              .collect(Collectors.toList());    
  }

  private void checkAdminAccess() {
        if (!currentUserService.isAdmin()) {
      throw new RuntimeException("Admin role required");
    }
  }
}
