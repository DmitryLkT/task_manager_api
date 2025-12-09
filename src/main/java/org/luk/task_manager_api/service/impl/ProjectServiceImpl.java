package org.luk.task_manager_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.luk.task_manager_api.dto.ProjectRequest;
import org.luk.task_manager_api.dto.ProjectResponse;
import org.luk.task_manager_api.model.Project;
import org.luk.task_manager_api.model.User;
import org.luk.task_manager_api.repository.ProjectRepository;
import org.luk.task_manager_api.repository.UserRepository;
import org.luk.task_manager_api.service.ProjectService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public List<ProjectResponse> getAllProjectsFromUser() {
    User user = getCurrentUser();

    List<Project> projects = projectRepository.findByOwner(user);
    
    return projects.stream()
                .map(p -> ProjectResponse.fromEntity(p))
                .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ProjectResponse getProject(String title) {
    Project project = projectRepository.findByTitle(title)
                  .orElseThrow(() -> new RuntimeException("Project not found"));
    return ProjectResponse.fromEntity(project);              
  }

  @Override
  @Transactional
  public ProjectResponse createdProject(ProjectRequest request) {
    if(projectRepository.existsByTitle(request.getTitle())) {
      throw new RuntimeException("Project with that title already exists.");
    }

    Project project = new Project(
      request.getTitle(), 
      request.getDescription(), 
      getCurrentUser()
    );
    
    projectRepository.save(project);

    return ProjectResponse.fromEntity(project);
  }

  @Override
  @Transactional
  public ProjectResponse updateProject(String title, ProjectRequest request) {
    Project project = projectRepository.findByTitle(title)
                  .orElseThrow(() -> new RuntimeException("Project not found"));
    
    if(request.getTitle() != null) {
      project.setTitle(request.getTitle());
    }

    if(request.getDescription() != null) {
      project.setDescription(request.getDescription());
    }

    projectRepository.save(project);

    return ProjectResponse.fromEntity(project);
  }

  @Override
  @Transactional
  public void deleteProject(String title) {
    Project project = projectRepository.findByTitle(title)
                  .orElseThrow(() -> new RuntimeException("Project not found"));
                 
    projectRepository.deleteById(project.getId());       
  }

  private User getCurrentUser() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return userRepository.findByEmail(email)
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
