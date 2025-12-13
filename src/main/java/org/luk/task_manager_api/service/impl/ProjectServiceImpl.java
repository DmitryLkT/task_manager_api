package org.luk.task_manager_api.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.luk.task_manager_api.dto.ProjectRequest;
import org.luk.task_manager_api.dto.ProjectResponse;
import org.luk.task_manager_api.model.Project;
import org.luk.task_manager_api.model.User;
import org.luk.task_manager_api.repository.ProjectRepository;
import org.luk.task_manager_api.service.CurrentUserService;
import org.luk.task_manager_api.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
  private final ProjectRepository projectRepository;
  private final CurrentUserService currentUserService;

  @Override
  @Transactional
  public List<ProjectResponse> getAllProjectsFromUser() {
    User user = currentUserService.getCurrentUser();

    List<Project> projects = projectRepository.findByOwner(user);
    
    return projects.stream()
                .map(p -> ProjectResponse.fromEntity(p))
                .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ProjectResponse getProject(String title) {
    User user = currentUserService.getCurrentUser();

    Project project = projectRepository.findByTitle(title)
                  .orElseThrow(() -> new RuntimeException("Project not found"));

    currentUserService.checkProjectOwnership(project, user);             

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
      currentUserService.getCurrentUser()
    );
    
    projectRepository.save(project);

    return ProjectResponse.fromEntity(project);
  }

  @Override
  @Transactional
  public ProjectResponse updateProject(String title, ProjectRequest request) {
    User user = currentUserService.getCurrentUser();
    Project project = projectRepository.findByTitle(title)
                  .orElseThrow(() -> new RuntimeException("Project not found"));

    currentUserService.checkProjectOwnership(project, user);              
    
    if(request.getTitle() != null && !request.getTitle().equals(project.getTitle())) {
      if(projectRepository.existsByTitle(request.getTitle())) {
        throw new RuntimeException("Title already taken");
      }
      
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
    User user = currentUserService.getCurrentUser();
    Project project = projectRepository.findByTitle(title)
                  .orElseThrow(() -> new RuntimeException("Project not found"));
                 
    currentUserService.checkProjectOwnership(project, user);
    
    projectRepository.delete(project);       
  }
}
